package com.example.m03_bounce;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.RawResourceDataSource;
import androidx.media3.exoplayer.ExoPlayer;

public class BouncingBallView extends View implements SensorEventListener {
    private Box box;

    public ExoPlayer getPlayer() {
        return player;
    }

    private ExoPlayer player;
    private TextView myTextView;

    public void setTemperatureDisplay(TextView tv) {
        this.myTextView = tv;
    }


    @OptIn(markerClass = UnstableApi.class)
    public BouncingBallView(Context context, AttributeSet attrs) {
        super(context, attrs);


        Log.v("BouncingBallView", "Constructor BouncingBallView");
        myTextView = findViewById(R.id.temperatureValue);

        player = new ExoPlayer.Builder(this.getContext()).build();
        Uri rawResourceUri = RawResourceDataSource.buildRawResourceUri(R.raw.nellyhotinhere);
        MediaItem mediaItem = MediaItem.fromUri(rawResourceUri);
        player.setMediaItem(mediaItem);
        player.prepare();


        // create the box
        box = new Box(Color.BLACK);  // ARGB

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float temperature = event.values[0];
            Log.v("temperatureChange", "temperature = " + temperature);
            myTextView.setText(String.valueOf(temperature));

            if(temperature > 20.0){
                player.setPlayWhenReady(true);
                Log.v("media", "mediaPlayer playing");
                myTextView.setTextColor(Color.RED);
            }else{
                player.setPlayWhenReady(false);
                Log.v("media", "mediaPlayer paused");
                myTextView.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v("onAccuracyChanged", "event=" + sensor.toString());
    }

}
