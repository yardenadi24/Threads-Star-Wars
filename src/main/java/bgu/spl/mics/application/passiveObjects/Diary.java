package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
//--fields--//
private AtomicInteger totalAttacks;
//--finish Task time stamp--//
private long CurrTime = System.currentTimeMillis();
private long HanSoloFinish;
private long C3POFinish;
private long R2D2Deactivate;
private long LandoAttacked;
//--termination time stamp--//
private long LeiaTerminate;
private long HanSoloTerminate;
private long C3POTerminate;
private long R2D2Terminate;
private long LandoTerminate;

    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    private Diary(){
        totalAttacks = new AtomicInteger(0);
    }
    //--get this singleton holder to work on--//
    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }

//--IncrementTotalAttacks--//
public void IncrementTotalAttacks(){
        int tempVal;
        do{
            tempVal = totalAttacks.get();

        }while(!totalAttacks.compareAndSet(tempVal,tempVal+1));
    }

    //--setters--//
public void setHanSoloFinish(){
    HanSoloFinish =System.currentTimeMillis();
}
public void setC3POFinish(){
    C3POFinish=System.currentTimeMillis();
    }
public void setR2D2Deactivate(){R2D2Deactivate=System.currentTimeMillis(); }
public void setLeiaTerminate(){
    LeiaTerminate=System.currentTimeMillis();
    }
public void setHanSoloTerminate(){
    HanSoloTerminate=System.currentTimeMillis();
    }
public void setC3POTerminate(){
    C3POTerminate=System.currentTimeMillis();
    }
public void setR2D2Terminate(){
    R2D2Terminate=System.currentTimeMillis();
    }
public void setLandoTerminate(){
    LandoTerminate=System.currentTimeMillis();
    }
public void setLandoAttacked(){
        LandoAttacked=System.currentTimeMillis();
    }

//--getters--//
public long GetHanSoloTerminate(){return HanSoloTerminate;}
public long GetC3POTerminate(){return C3POTerminate;}
public long GetR2D2Terminate(){return R2D2Terminate;}
public long GetLandoTerminate(){return LandoTerminate;}
public long GetLeiaTerminate(){return LeiaTerminate;}
public long GetHanSoloFinish(){return HanSoloFinish;};
public long GetC3POFinish(){return C3POFinish;};
public long GetR2D2Deactivate(){return R2D2Deactivate;};
public AtomicInteger GetNumberOfAttacks(){return totalAttacks;}
public long GetLandoAttacked(){return LandoAttacked;}

public void resetNumberAttacks(){totalAttacks = new AtomicInteger(0);}

public AtomicInteger GetTotalAttacks(){return totalAttacks;}
}
