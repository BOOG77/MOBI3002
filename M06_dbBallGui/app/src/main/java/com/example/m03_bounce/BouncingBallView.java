package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BouncingBallView extends View {


    private ArrayList<Ball> balls = new ArrayList<Ball>(); // list of Balls
    private Ball ball_1;  // use this to reference first ball in arraylist
    private Box box;

    DBClass DBtest;  // class level declaration

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;
    private Paint textPaint;
    String defaultName = "dawson";
    static int nameId = 0;

    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.v("BouncingBallView", "Constructor BouncingBallView");

        // create the box
        box = new Box(Color.LTGRAY);  // ARGB

        //ball_1 = new Ball(Color.GREEN);
//        balls.add(new Ball(Color.GREEN));
//        ball_1 = balls.get(0);  // points ball_1 to the first; (zero-ith) element of list
//        Log.w("BouncingBallLog", "Just added a bouncing ball");

        //ball_2 = new Ball(Color.CYAN);
//        balls.add(new Ball(Color.CYAN));
//        Log.w("BouncingBallLog", "Just added another bouncing ball");

        // Get from DB
        DBtest = new DBClass(context);
        List<DataModel> ALL = DBtest.findAll();
        for (DataModel one : ALL) {
            Log.w("DataModel", "Item => " + one.toString());
            balls.add(new Ball(one.getColor(), one.getModelX(),
                    one.getModelY(), one.getModelDX(), one.getModelDY(), one.getName()));
        }


        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    private void init(){

    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {

        //Log.v("BouncingBallView", "onDraw");

        // Draw the components
        box.draw(canvas);
        //canvas.drawARGB(0,25,25,25);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        for (Ball b : balls) {
            float textX = b.getX();
            float textY = b.getY() - 20f;
            b.draw(canvas);  //draw each ball in the list
            canvas.drawText(b.getName(), textX, textY, textPaint);
            b.moveWithCollisionDetection(box);  // Update the position of the ball
        }

        // Delay on UI thread causes big problems!
        // Simulates doing busy work or waits on UI (DB connections, Network I/O, ....)
        //  I/Choreographer? Skipped 64 frames!  The application may be doing too much work on its main thread.
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//        }

        // Check what happens if you draw the box last
        //box.draw(canvas);

        // Check what happens if you don't call invalidate (redraw only when stopped-started)
        // Force a re-draw
        this.invalidate();
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                ball_1.speedX += deltaX * scalingFactor;
                ball_1.speedY += deltaY * scalingFactor;
                //Log.w("BouncingBallLog", " Xspeed=" + ball_1.speedX + " Yspeed=" + ball_1.speedY);
                Log.w("BouncingBallLog", "x,y= " + previousX + " ," + previousY + "  Xdiff=" + deltaX + " Ydiff=" + deltaY);
                String name = "touchEventName";
                balls.add(new Ball(Color.BLUE, previousX, previousY, deltaX, deltaY, name));  // add ball at every touch event

                // A way to clear list when too many balls
                if (balls.size() > 20) {
                    // leave first ball, remove the rest
                    Log.v("BouncingBallLog", "too many balls, clear back to 1");
                    balls.clear();
                    balls.add(new Ball(Color.RED));
                    ball_1 = balls.get(0);  // points ball_1 to the first (zero-ith) element of list
                }

        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;

        // Try this (remove other invalidate(); )
        //this.invalidate();

        return true;  // Event handled
    }

    public void clearButtonPressed(){
        balls.clear();
    }
    Random rand = new Random();

    // called when button is pressed
    public void DawsonButtonPressed(int string_color, float x, float y, float dx, float dy, String name) {
        Log.d("BouncingBallView  BUTTON", "User tapped the  button ... VIEW");

        balls.add(new Ball(string_color, x, y, dx, dy, name));  // add ball at every touch event

        Log.v("BouncingBallView  BUTTON", "n...add ball to DB");
        DataModel newBall = new DataModel(0, x, y, dx, dy, string_color, name);
        DBtest.save(newBall);
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }
}
