package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long _duration;
    private Diary diary=Diary.getInstance();
    private MessageBusImpl msb=MessageBusImpl.getInstance();
    public LandoMicroservice(long duration){
        super("Lando");
      _duration =duration;
    }

    @Override
    protected void initialize() {

        subscribeEvent(BombDestroyerEvent.class, ev->{

           try{
           Thread.sleep(_duration);
           }catch(InterruptedException e){}


            diary.setLandoAttacked();
            msb.complete(ev,true);

        });
        subscribeBroadcast(TerminationBroadcast.class,ev->{
            diary.setLandoTerminate();
            terminate();
        });

    }
}
