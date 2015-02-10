package com.example.n971873.stopper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class MyView extends View {

    private final long MAXORA = 60000;
    private Paint paint;
    private Paint paintHand;
    private Paint paintArc;
    private long timerMaxOra;
    private long stopperMaxOra;
    private long centerX;
    private long centerY;
    private long radius;
    private RectF rectangle;
    private long startTime;
    private boolean menet;
    private double prevSzog = 0;
    public boolean timer;
    private boolean defaultState;
    private Globals globals;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);

        paintHand = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHand.setColor(Color.BLACK);
        paintHand.setStrokeWidth(5);

        paintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArc.setColor(Color.YELLOW);

        Paint paintArc2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArc2.setColor(Color.RED);

        timer = false;
        rectangle = new RectF(0, 0, 0, 0);
        setTimerMaxOra(MAXORA);
        setStopperMaxOra(MAXORA);

        setDefaultState(true);

        globals = Globals.getInstance();
        globals.getTimerMaxOra();
    }

    public void initStart() {
        setStartTime(System.currentTimeMillis());
        menet = true;
        prevSzog = 0;
        setDefaultState(false);
    }

    public void setDefaultState(boolean inFlag) {
        defaultState = inFlag;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        centerX = Math.round(this.getHeight() / 2);
        centerY = Math.round(this.getWidth() / 2);
        radius = Math.round(Math.min(centerY, centerY) * 0.80);
        long arcRadius = Math.round(radius / 2);
        rectangle.set(centerX - arcRadius, centerY - arcRadius,
                centerX + arcRadius, centerY + arcRadius);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        double szog;
        int szog2;
        super.onDraw(canvas);

        if (defaultState) {
            szog = 0;
            szog2 = 0;
        } else {
            if (timer) {
                szog = (float) ((getTimerMaxOra() - getElapsedTime()) % getTimerMaxOra()) / getTimerMaxOra() * 2 * Math.PI;
                szog2 = Math.round((float) (getTimerMaxOra() - getElapsedTime()) % getTimerMaxOra() / getTimerMaxOra() * 360);
            } else {
                szog = (float) (getElapsedTime() % getStopperMaxOra()) / getStopperMaxOra() * 2 * Math.PI;
                if (szog < prevSzog) menet = !menet;
                prevSzog = szog;
                szog2 = Math.round((float) getElapsedTime() % getStopperMaxOra() / getStopperMaxOra() * 360);
            }
        }
        double fx = Math.sin(szog) * radius;
        double fy = Math.cos(szog) * radius;
        fy *= -1;
        fx += centerX;
        fy += centerY;

        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.drawLine(centerX, centerY, Math.round(fx), Math.round(fy), paintHand);

        if (menet) canvas.drawArc(rectangle, 270, szog2, true, paintArc);
        else canvas.drawArc(rectangle, szog2 + 270 % 360, 360 - szog2, true, paintArc);
        Singleton kaki=Singleton.getInstance();
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

    public long getElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - getStartTime();
        if (timer && elapsedTime > getTimerMaxOra()) return getTimerMaxOra();
        else return elapsedTime;
    }
}

