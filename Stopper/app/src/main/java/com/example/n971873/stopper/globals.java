package com.example.n971873.stopper;

/**
 * Created by N971873 on 2015.02.09..
 */
public class Globals extends Object{
    private static Globals objGlobals;

    public long getTimerMaxOra() {
        return timerMaxOra;
    }

    public void setTimerMaxOra(long timerMaxOra) {
        this.timerMaxOra = timerMaxOra;
    }

    private long timerMaxOra;

    public Globals() {
    }

    public static Globals getInstance() {
        if (objGlobals == null) {
            objGlobals=new Globals();
            return objGlobals;
        }
        else return objGlobals;
    }

}
