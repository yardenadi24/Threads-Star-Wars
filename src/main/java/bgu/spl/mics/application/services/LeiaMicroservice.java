package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.*;
import bgu.spl.mics.application.AllSubSingle;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private Diary diary=Diary.getInstance();
    private MessageBusImpl msb= MessageBusImpl.getInstance();
    private List<Event> eventList;
    private ConcurrentHashMap<Event,Future> futureMap;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futureMap = new ConcurrentHashMap<Event,Future>();
    }

    @Override
    protected void initialize() {


        //--creat indicator for "is every one subscribed yet"--//
        AllSubSingle subscount = AllSubSingle.getInstance();

       //--checks if everyone subscribed--//
        while(subscount.getSubsCount()<2){
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){}
        }

        //--creating attack events and sending them--//
        for(Attack _attack:attacks){
           AttackEvent temp =  new AttackEvent(_attack);
            Future future = null;

        //--checks that every future we created is valid--//
           while(future==null){
               future= sendEvent(temp);
           }
            futureMap.put(temp,future);
        }

        //Leia loops over the future map checking for resolved futures
        for(Map.Entry<Event,Future> iter:futureMap.entrySet()){
            iter.getValue().get(1, TimeUnit.MILLISECONDS);
        }

        futureMap.clear();

        //--after exiting while loop there are no more Attack events to resolve--//
        //--send deactivation event to R2D2--//
        DeactivationEvent temp =  new DeactivationEvent();
        //--save the relevant future of the event--//
        futureMap.put(temp,sendEvent(temp));


        //--Leia loops over the future map checking for resolved futures--//
        for(Map.Entry<Event,Future> iter:futureMap.entrySet()){
            iter.getValue().get(1, TimeUnit.MILLISECONDS);
        }

        futureMap.clear();

        //--Leia sends Bomb event to Lando after Deactivation future got resolved--//
        BombDestroyerEvent temp1 = new BombDestroyerEvent();
        futureMap.put(temp1,sendEvent(temp1));

        //--Leia loops over the future map checking for resolved futures--//
        for(Map.Entry<Event,Future> iter:futureMap.entrySet()){
            iter.getValue().get(1, TimeUnit.MILLISECONDS);
        }
        //-- after bomb event got resolved leia sends termination broadcast then terminate herself--//
        sendBroadcast(new TerminationBroadcast());
        diary.setLeiaTerminate();
        terminate();
    }
}
