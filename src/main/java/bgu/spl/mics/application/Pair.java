package bgu.spl.mics.application;

public class Pair<First,Second> {
    private First firstObject; //key
    private Second secondObject; //value

    //--constructor--//
    public Pair(First first,Second sec){
        this.firstObject = first;
        this.secondObject= sec;
    }

    //--get key--//
    public First getFirst(){return firstObject;}
    //--get val--//
    public Second getSecond(){return secondObject;}

}

