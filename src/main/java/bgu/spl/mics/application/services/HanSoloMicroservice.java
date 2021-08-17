package bgu.spl.mics.application.services;


import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.AllSubSingle;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

/**
 * HanSoloMicroservices is in charge of the handling {@link bgu.spl.mics.application.messages.AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link bgu.spl.mics.application.messages.AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    //--fields--//
    private Ewoks ewoks=Ewoks.getInstance();
    private Diary diary=Diary.getInstance();
    private MessageBusImpl msb=MessageBusImpl.getInstance();
    private boolean inc = false;
    //--constructor--//
    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {
        AllSubSingle subscount = AllSubSingle.getInstance();

        subscribeEvent(AttackEvent.class,ev-> {

            Attack newAttack = ev.getAttack();
            List<Integer> serials = newAttack.getSerials();

            for (int i = 0; i < serials.size(); i++) {
                ewoks.useEwok(i);
            }

            try {
                Thread.sleep(newAttack.getDuration());

                   diary.IncrementTotalAttacks();


            } catch (InterruptedException e) {
                e.fillInStackTrace();
            }
            for (int i = 0; i < serials.size(); i++) {
                ewoks.finishUse(i);
            }

            diary.setHanSoloFinish();
            msb.complete(ev,true);

        });
        subscount.IncSubs();

        subscribeBroadcast(TerminationBroadcast.class,ev->{
            diary.setHanSoloTerminate();
            terminate();
        });

    }
}
