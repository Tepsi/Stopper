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
    private long centerX;
    private long centerY;
    private long radius;
    private RectF rectangle;
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
        rectangle = new RectF(0, 0, 0, 0);

        globals = Globals.getInstance();
        globals.timer = false;

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
        super.onDraw(canvas);

        double fx = Math.sin(globals.szog) * radius;
        double fy = Math.cos(globals.szog) * radius;
        fy *= -1;
        fx += centerX;
        fy += centerY;

        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.drawLine(centerX, centerY, Math.round(fx), Math.round(fy), paintHand);

        if (globals.menet) canvas.drawArc(rectangle, 270, globals.szog2, true, paintArc);
        else canvas.drawArc(rectangle, globals.szog2 + 270 % 360, 360 - globals.szog2, true, paintArc);
    }

}

