package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.Pair;

import java.util.Vector;

public class JsonInPut {
    private Attack[] attacks;
    private int R2D2;
    private int Lando;
    private int Ewoks;


    public JsonInPut(Attack[] _attacks,int _R2D2,int _Lando,int _Ewoks){
        attacks = _attacks;
        R2D2 = _R2D2;
        Lando = _Lando;
        Ewoks = _Ewoks;
    }

    //---getters--//
    public int getEwoks() {
        return Ewoks;
    }

    public int getLando() {
        return Lando;
    }

    public int getR2D2() {
        return R2D2;
    }

    public Attack[] getAttacks() {
        return attacks;
    }

}
