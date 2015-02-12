package com.example.n971873.stopper;

import android.view.View;

/**
 * Created by N971873 on 2015.02.06..
 */
class ButtonRunnable implements Runnable{
    private View view;
    private Globals globals;
    public ButtonRunnable(View inView) {
        view=inView;
        globals=Globals.getInstance();
    }

    public void run() {
        long changeRate;
        switch (view.getId()) {
            case R.id.buttonSecondUp:
                changeRate=1000;
                break;
            case R.id.buttonSecondDown:
                changeRate=-1000;
                break;
            case R.id.buttonMinuteUp:
                changeRate=+60000;
                break;
            case R.id.buttonMinuteDown:
                changeRate=-60000;
                break;
            default:
                changeRate=0;
        }
        globals.setTimerMaxOra(globals.getTimerMaxOra()+changeRate);

    }
}
