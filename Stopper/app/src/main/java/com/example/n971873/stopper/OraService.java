package com.example.n971873.stopper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OraService extends Service {

    private Timer timer;
    private Handler handler;
    private Globals globals;

    public OraService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        globals=Globals.getInstance();
        timer = new Timer();
        timer.scheduleAtFixedRate(new OraTask(this),0,100);
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    class OraTask extends TimerTask
    {
        private Context context;
        private boolean sound = false;
        private double prevSzog;

        OraTask(Context inContext) {
            super();
            context=inContext;
            sound=false;
            prevSzog=0;
            globals.setStartTime(System.currentTimeMillis());
            globals.menet = true;
            globals.setDefaultState(false);
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsedTime;
                    if (globals.timer) {
                        globals.szog = (float) ((globals.getTimerMaxOra() -
                                                 globals.getElapsedTime()) %
                                globals.getTimerMaxOra()) / globals.getTimerMaxOra() * 2 * Math.PI;
                        globals.szog2 = Math.round((float) (globals.getTimerMaxOra() -
                                    globals.getElapsedTime()) % globals.getTimerMaxOra() /
                                globals.getTimerMaxOra() * 360);
                    } else {
                        globals.szog = (float) (globals.getElapsedTime() %
                                globals.getStopperMaxOra()) / globals.getStopperMaxOra() * 2 * Math.PI;
                        if (globals.szog < prevSzog) globals.menet = !globals.menet;
                        prevSzog = globals.szog;
                        globals.szog2 = Math.round((float) globals.getElapsedTime() % globals.getStopperMaxOra() / globals.getStopperMaxOra() * 360);
                    }

                    elapsedTime = globals.getTimerMaxOra()-
                            globals.getElapsedTime();

                    if (globals.timer) {
                        if (elapsedTime==0 && !sound) {
                            try {
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                r.play();
                                sound=true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            context.stopService(new Intent(context, OraService.class));
                        }
                    }

                    long minute = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
                    long second = Math.round((elapsedTime-60000*minute)/1000);
                    globals.editText.setText(String.format(
                            "%02d:%02d:%03d",minute,second,elapsedTime%1000));

                    globals.myView.invalidate();
                }
            });
        }

    }


}
