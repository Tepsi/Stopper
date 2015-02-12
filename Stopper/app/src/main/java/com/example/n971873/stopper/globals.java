package com.example.n971873.stopper;

import android.widget.Button;
import android.widget.EditText;

/**
 * Created by N971873 on 2015.02.09..
 */
public class Globals {
    private static Globals objGlobals;
    public MyView myView;
    public EditText editText;
    public boolean oraStarted,stop,timer,defaultState,menet;
    public Button button;
    public long startTime, timerMaxOra, stopperMaxOra;
    public double szog;
    public int szog2;

    public Globals() {
    }

    public static Globals getInstance() {
        if (objGlobals == null) {
            objGlobals=new Globals();
            return objGlobals;
        }
        else return objGlobals;
    }

    public long getElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (timer && elapsedTime > timerMaxOra) return timerMaxOra;
        else return elapsedTime;
    }

    public long getStopperMaxOra() {
        return stopperMaxOra;
    }

    public void setStopperMaxOra(long maxOra) {
        this.stopperMaxOra = maxOra;
        if (this.stopperMaxOra < 1000) this.stopperMaxOra = 1000;
    }

    public long getTimerMaxOra() {
        return timerMaxOra;
    }

    public void setTimerMaxOra(long maxOra) {
        this.timerMaxOra = maxOra;
        if (this.timerMaxOra < 1000) this.timerMaxOra = 1000;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setDefaultState(boolean inFlag) { defaultState = inFlag; }


}
