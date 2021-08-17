package bgu.spl.mics;
import bgu.spl.mics.application.Pair;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;




/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	//--MassageBus fields--//
	///Map of microservices which tells us which events and broadcasts each microservice subs too
    private ConcurrentHashMap<MicroService, Pair<Vector<Class<? extends Broadcast>>,Vector<Class<? extends Event>>>> microServiceQueMap;
    ///Map of messages Ques for each microservice
	private ConcurrentHashMap <MicroService, LinkedBlockingQueue<Message>> messagesQueueMap;
	///Map that tells us which microservice turn is it to get message
	private ConcurrentHashMap <Class<? extends Event>, Pair<Vector<MicroService>, AtomicInteger>> roundRobinMap;
	//Map of futures for each event
	private ConcurrentHashMap <Event,Future> futureMap;

	//--build singleton holder--//
	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}


	//--constructor--//
	private MessageBusImpl(){
		microServiceQueMap = new ConcurrentHashMap<>();
		messagesQueueMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
		roundRobinMap = new ConcurrentHashMap<>();
	}

	//--get this singleton holder to work on--//
	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;

	}
	
	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		microServiceQueMap.get(m).getSecond().add(type);
		//if this type of event dose not appear in the round robin map add a pair of the type and list of ms who should handle this type
		if(!roundRobinMap.containsKey(type)){
			roundRobinMap.put(type, new Pair<>( new Vector<>(), new AtomicInteger(0)));
		}
		//add to the list of ms working on the event our ms "m"
		roundRobinMap.get(type).getFirst().add(m);
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		microServiceQueMap.get(m).getFirst().add(type);

    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) { futureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//send a broadcast message to all ms subscribed to this type of broadcast
		for(Map.Entry<MicroService,Pair<Vector<Class<? extends Broadcast>>,Vector<Class<?extends Event>>>> iter:microServiceQueMap.entrySet()){
			if(iter.getValue().getFirst().contains(b.getClass())){
				try{ messagesQueueMap.get(iter.getKey()).put(b); }
				catch(InterruptedException e){}
			}
		}
	}

	
	@Override

	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = new Future<>();
		//add future who will be resolved later connected to the event
		futureMap.put(e, future);
		try{
		//get the list of microservices who can handle this event
		Pair<Vector<MicroService>,AtomicInteger> pair = roundRobinMap.get(e.getClass());
		//check if some one is subscribed to this event
		if(pair.getFirst().size()>0){
			//which micro service should handle this event round robin wise
			AtomicInteger i = new AtomicInteger((pair.getSecond().getAndIncrement()) % (pair.getFirst().size()));
			MicroService m = pair.getFirst().get(i.get());
			messagesQueueMap.get(m).put(e);
		} else {
			return null;
		}
		}catch(Exception ex){
			return null;
		}
        return future;
	}

	@Override
	public void register(MicroService m) {
		//create vector of events and vector of broadcast to store the subscribed types
		Vector<Class<? extends Event>> eventsVec = new Vector<>();
		Vector<Class<? extends Broadcast>> broadVec = new Vector();
		Pair<Vector<Class<? extends Broadcast>>,Vector<Class<? extends Event>>> pair = new Pair<>(broadVec,eventsVec);
		//store pair of vectors in the map with unique key for this microservice
		microServiceQueMap.put(m,pair);
		//stores a unique message que for the micro service
		messagesQueueMap.put(m,new LinkedBlockingQueue<>());


	}

	@Override
	public void unregister(MicroService m) {
		LinkedBlockingQueue<Message> tempQue = messagesQueueMap.get(m);
		microServiceQueMap.remove(m);
		for(Message event:tempQue){
			try{
				futureMap.get(event).resolve(null);
			}catch (NullPointerException e){ e.fillInStackTrace();}
		}
		//clean m from message que
		messagesQueueMap.remove(m);
		//delete
		for(Pair<Vector<MicroService>,AtomicInteger> RoundRobinList: roundRobinMap.values()){
			//remove the microservice from the list of ms
			RoundRobinList.getFirst().remove(m);
		}
		
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		//using m Key to take message if available
		return messagesQueueMap.get(m).take();
	}


}
