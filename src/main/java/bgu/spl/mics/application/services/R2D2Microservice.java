package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link bgu.spl.mics.application.messages.DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link bgu.spl.mics.application.messages.DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long _duration;
    private Diary diary=Diary.getInstance();
    private MessageBusImpl msb=MessageBusImpl.getInstance();
    public R2D2Microservice(long duration) {
        super("R2D2");
        _duration = duration;
    }

    @Override
    protected void initialize() {

        subscribeEvent(DeactivationEvent.class,ev->{

            try{
                Thread.sleep(_duration);
            }catch(InterruptedException e){}

            diary.setR2D2Deactivate();
            msb.complete(ev,true);
        });

        subscribeBroadcast(TerminationBroadcast.class, ev->{
            diary.setR2D2Terminate();
            terminate();
        });

    }
}
