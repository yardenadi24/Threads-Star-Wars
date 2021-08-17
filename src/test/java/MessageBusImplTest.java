import bgu.spl.mics.Future;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.services.R2D2Microservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class MessageBusImplTest {
    MessageBus msb;
    TerminationBroadcast b;
    DeactivationEvent ev;
    R2D2Microservice r2d2;


    @BeforeEach
    void setUp() {
        msb = MessageBusImpl.getInstance();
        ev = new DeactivationEvent();
        b = new TerminationBroadcast();
        r2d2 = new R2D2Microservice(1000);
    }

    @Test
    void complete() {
        boolean str = true;
        msb.register(r2d2);
        msb.subscribeEvent(ev.getClass(),r2d2);
        Future<Boolean> fut = msb.sendEvent(ev);
        //pre condition: future result is null , isDone is false
        assertFalse(fut.isDone());
        msb.complete(ev,str);
        //post condition: future result is true, isDone is true
        assertTrue(fut.get());
        assertTrue(fut.isDone());
    }

    @Test
    void sendBroadcast() throws InterruptedException {
        msb.register(r2d2);
        msb.subscribeBroadcast(b.getClass(),r2d2);
        msb.sendBroadcast(b);

        Message msg = null;
        //pre condition message null
        try {
            msg = msb.awaitMessage(r2d2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //post condition msg is not null
        assertTrue(msg!=null);

    }

    @Test
    void sendEvent() {
        //NO MESSAGES AT "M" QUEUE
        msb.register(r2d2);
        msb.subscribeEvent(ev.getClass(),r2d2);
        msb.sendEvent(ev);
        Message msg = null;
        try {
            msg = msb.awaitMessage(r2d2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //post condition the event was found in M queue
        assertTrue(msg!=null);

        msb.sendBroadcast(b);
    }
}