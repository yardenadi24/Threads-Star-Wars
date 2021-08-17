package bgu.spl.mics.application.passiveObjects;
import  bgu.spl.mics.MessageBusImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private ArrayList<Ewok> ewoksMap;
    private Object key;
    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }
    //--get this singleton holder to work on--//
    public static Ewoks getInstance() {
        return Ewoks.SingletonHolder.instance;
    }

    private Ewoks(){
        ewoksMap = new ArrayList<Ewok>();
        key = new Object();
    }

    public ArrayList<Ewok> getEwoksMap(){
        return ewoksMap;
    }

    public void addEwok(Ewok _ewok){
        ewoksMap.add(_ewok);
    }

    public void useEwok(int SerialEwok){
        /**get ewok from map using the serial only??**/
    synchronized (key){
        while(!ewoksMap.get(SerialEwok).available){
            try{
                key.wait();
            }
            catch (InterruptedException e){}
        }
        ewoksMap.get(SerialEwok).acquire();
    }
    }

    public void finishUse(int SerialEwok) {
        /**get ewok from map using the serial only??**/
        synchronized (key){
            ewoksMap.get(SerialEwok).release();
            key.notifyAll();
        }
    }
}
