package com.example.n971873.stopper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener {

//    private final String TAG="MainActivity";
    private final String TAG=this.toString();
    private final long MAXORA=60000;
    private SharedPreferences settings;
    private Globals globals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals=Globals.getInstance();
        globals.oraStarted = false;
        setContentView(R.layout.sample_my_view);
        globals.editText=(EditText) findViewById(R.id.editText);
        globals.myView = (MyView) findViewById(R.id.oraView);
        globals.button = (Button) findViewById(R.id.button);
        settings = getPreferences(MODE_PRIVATE);
        globals.setTimerMaxOra(settings.getLong("timerMaxOra",MAXORA));
        globals.setStopperMaxOra(MAXORA);
        globals.setDefaultState(true);
        Button buttonMinuteUp = (Button) findViewById(R.id.buttonMinuteUp);
        Button buttonMinuteDown = (Button) findViewById(R.id.buttonMinuteDown);
        Button buttonSecondUp = (Button) findViewById(R.id.buttonSecondUp);
        Button buttonSecondDown = (Button) findViewById(R.id.buttonSecondDown);
        buttonMinuteDown.setOnTouchListener(this);
        buttonMinuteUp.setOnTouchListener(this);
        buttonSecondDown.setOnTouchListener(this);
        buttonSecondUp.setOnTouchListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void oraStart(View view) {
        Button button = (Button) findViewById(R.id.button);
        Button timerButton = (Button) findViewById(R.id.timerButton);
        Button stopperButton = (Button) findViewById(R.id.stopperButton);
        String TAG = "MainActivity";
        if (globals.oraStarted) {
            Log.d(TAG,"Ora stopped");
            stopService(new Intent(this, OraService.class));
            globals.oraStarted = false;
            button.setText(R.string.buttonStart);
            timerButton.setEnabled(!globals.timer);
            stopperButton.setEnabled(globals.timer);
            switchTimeSelector(true);
            globals.myView.invalidate();
            if (globals.timer) refreshDigitalClock(globals.getTimerMaxOra());

        }
        else {
            Log.d(TAG,"Ora started");
            globals.setStartTime(System.currentTimeMillis());
            button.setText(R.string.buttonStop);
            globals.oraStarted = true;
            globals.stop = false;
            startService(new Intent(this, OraService.class));
            stopperButton.setEnabled(false);
            timerButton.setEnabled(false);
            switchTimeSelector(false);
            if (globals.timer) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("timerMaxOra",globals.getTimerMaxOra());
                editor.apply();
            }

        }
    }

    public void switchTimeSelector(boolean inFlag) {
        boolean flag=inFlag;

        Button buttonMinuteUp = (Button) findViewById(R.id.buttonMinuteUp);
        Button buttonMinuteDown = (Button) findViewById(R.id.buttonMinuteDown);
        Button buttonSecondUp = (Button) findViewById(R.id.buttonSecondUp);
        Button buttonSecondDown = (Button) findViewById(R.id.buttonSecondDown);
        if (!globals.timer) flag=false;
        buttonMinuteUp.setEnabled(flag);
        buttonMinuteDown.setEnabled(flag);
        buttonSecondUp.setEnabled(flag);
        buttonSecondDown.setEnabled(flag);
    }

    public void timerClick(View view) {
        Button timerButton = (Button) findViewById(R.id.timerButton);
        Button stopperButton = (Button) findViewById(R.id.stopperButton);
        stopperButton.setEnabled(true);
        timerButton.setEnabled(false);
        globals.timer=true;
        switchTimeSelector(true);
        globals.setTimerMaxOra(globals.getTimerMaxOra());
        refreshDigitalClock(globals.getTimerMaxOra());
    }

    public void stopperClick(View view) {
        Button timerButton = (Button) findViewById(R.id.timerButton);
        Button stopperButton = (Button) findViewById(R.id.stopperButton);
        stopperButton.setEnabled(false);
        timerButton.setEnabled(true);
        globals.timer=false;
        switchTimeSelector(false);
        globals.setTimerMaxOra(globals.getStopperMaxOra());
        refreshDigitalClock(0);
    }

    private void refreshDigitalClock() {
        long maxOra;
        if (globals.timer) maxOra=globals.getTimerMaxOra();
        else maxOra=globals.getStopperMaxOra();
        long minute = TimeUnit.MILLISECONDS.toMinutes(maxOra);
        long second = Math.round((maxOra-60000*minute)/1000);
        globals.editText.setText(String.format(
                "%02d:%02d:%03d",minute,second,maxOra%1000));
    }

    private void refreshDigitalClock(long time) {
        long minute = TimeUnit.MILLISECONDS.toMinutes(time);
        long second = Math.round((time-60000*minute)/1000);
        globals.editText.setText(String.format(
                "%02d:%02d:%03d",minute,second,time%1000));
    }

    public boolean onTouch(View inView, MotionEvent event) {
        Handler handler=null;
        ButtonRunnable action=null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (handler==null) {
                    handler=new Handler();
                    action = new ButtonRunnable(inView);
                    handler.postDelayed(action,500);
                }
                break;
            default:
                if (handler!=null && action!=null) {
                    handler.removeCallbacks(action);
                    action=null;
                    handler=null;
                }
        }
        refreshDigitalClock();
        return true;
    }


}
