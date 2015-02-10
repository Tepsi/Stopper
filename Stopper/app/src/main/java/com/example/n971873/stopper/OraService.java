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

    private MyApplication application;
    private Timer timer;
    private Handler handler;

    public OraService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application =  (MyApplication) this.getApplication();
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

        OraTask(Context inContext) {
            super();
            context=inContext;
            sound=false;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    application.myView.invalidate();
                    long elapsedTime;
                    if (application.myView.timer) {
                        elapsedTime = application.myView.getTimerMaxOra()-
                                           application.myView.getElapsedTime();
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
                        };
                    }
                    else {
                        elapsedTime = application.myView.getElapsedTime();
                        if (elapsedTime>application.myView.getStopperMaxOra())
                            application.myView.setStopperMaxOra(
                                application.myView.getStopperMaxOra()*60);
                    }
                    long minute = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
                    long second = Math.round((elapsedTime-60000*minute)/1000);
                    application.editText.setText(String.format(
                            "%02d:%02d:%03d",minute,second,elapsedTime%1000));

                }
            });
        }

    }


}
