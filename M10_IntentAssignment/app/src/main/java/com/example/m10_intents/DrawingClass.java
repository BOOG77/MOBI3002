package com.example.m10_intents;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.InputStream;
import java.util.ArrayList;
import androidx.annotation.Nullable;

class PathAndPaint {
    public Path path;
    public Paint paint;

    public PathAndPaint(Path path, Paint paint){
        this.path = path;
        this.paint = paint;
    }

}

public class DrawingClass extends View {

    // drawing values
    private Path drawPath; // position of the lines you are drawing
    private Paint drawPaint; // drawing properties
    private Paint canvasPaint; // background colour for when we aren't using an image
    private ArrayList<PathAndPaint> paths; // arraylist to remember the lines we've drawn

    // drawing coordinates
    private float lastX;
    private float lastY;

    // background
    private Bitmap backgroundBitmap;
    private Paint backgroundPaint;

    // required constructor for view
    public DrawingClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paths = new ArrayList<>();
        backgroundPaint = new Paint(Color.WHITE);
        canvasPaint = new Paint(Color.WHITE);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPaint = new Paint();

        // drawing properties
        drawPaint.setColor(Color.RED);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(12f);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        drawPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (backgroundBitmap != null) {
            Rect srcRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
            Rect destRect = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(backgroundBitmap, srcRect, destRect, backgroundPaint);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        // draw all the saved lines from the list
        for (PathAndPaint p : paths) {
            canvas.drawPath(p.path, p.paint);
        }

        // draw the newest line being drawn currently
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchY = event.getY();
        float touchX = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(event.getX(), event.getY());
                lastX = touchX;
                lastY = touchY;
                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(event.getX(), event.getY());
                lastX = touchX;
                lastY = touchY;
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(lastX, lastY);
                paths.add(new PathAndPaint(new Path(drawPath), new Paint(drawPaint)));
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setBackgroundUri(Uri imageUri){
        try{
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            backgroundBitmap = BitmapFactory.decodeStream(inputStream);
            if(inputStream != null){
                inputStream.close();
            }

            paths.clear();
            drawPath.reset();
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearDrawings() {
        paths.clear();
        drawPath.reset();
        invalidate();
    }
}

