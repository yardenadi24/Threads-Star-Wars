package bgu.spl.mics.application;

import java.util.concurrent.atomic.AtomicInteger;

public class AllSubSingle {
    private AtomicInteger subCount;
    private static AllSubSingle single_sub_instace = null;

    private AllSubSingle(){
        subCount = new AtomicInteger(0);
    }

    public static synchronized AllSubSingle getInstance(){
        if(single_sub_instace == null){
            single_sub_instace = new AllSubSingle();
        }
        return single_sub_instace;
    }

    public void IncSubs(){
        subCount.getAndIncrement();
    }
    public int getSubsCount(){
        return subCount.intValue();
    }

}
