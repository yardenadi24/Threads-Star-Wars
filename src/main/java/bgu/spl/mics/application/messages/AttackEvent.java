package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private Attack attack;

    //--empty constructor--//
    public AttackEvent(){};

    //--constructor--//
    public AttackEvent(Attack _attack){
        this.attack = _attack;
    }
    //--getter--//
    public Attack getAttack(){
        return attack;
    }

}
