package com.example.m03_bounce;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Found tutorial to do put buttons over view here:
// https://code.tutsplus.com/tutorials/android-sdk-creating-custom-views--mobile-14548

public class MainActivity extends AppCompatActivity {


    // bbView is our bouncing ball view
    private BouncingBallView bbView;
    DBClass dbTest;
    long id = 1;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get the view object so we can reference it later
        bbView = (BouncingBallView) findViewById(R.id.custView);
        dbTest = new DBClass(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        final DBClass dbLocal = dbTest;
        final BouncingBallView viewLocal = bbView;


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dbTest.emptyTable();
                id = 1;
                List<DataModel> dataList = new ArrayList<>();
                List<Ball> balls = viewLocal.getBalls();
                for(Ball b : balls) {
                    DataModel dataBall = new DataModel(id, b.getX(), b.getY(), b.getSpeedX(), b.getSpeedY(), b.getColor(), b.getName());
                    dataList.add(dataBall);
                    id++;
                }

                if (!dataList.isEmpty()) {
                    dbLocal.saveList(dataList);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Good practice: Shut down the executor and set the DB reference to null
        executorService.shutdown();
        dbTest = null;
    }

    // button action

    public void onClearButtonClick(View v){
        bbView.clearButtonPressed();
        dbTest.emptyTable();
    }

    public void onDawsonButtonClick(View v) {

        Log.d("MainActivity  BUTTON", "User tapped the  button ... MAIN");

        // get spinner values
        SeekBar seekbar_color = (SeekBar) findViewById(R.id.seekBar_Color);
        SeekBar seekbar_x = (SeekBar) findViewById(R.id.seekBar_X);
        SeekBar seekbar_y = (SeekBar) findViewById(R.id.seekBar_Y);
        SeekBar seekbar_dx = (SeekBar) findViewById(R.id.seekBar_DX);
        SeekBar seekbar_dy = (SeekBar) findViewById(R.id.seekBar_DY);
        EditText nameInput = findViewById(R.id.nameInput);
        Editable editableText = nameInput.getText();
        String extractedNameInput = editableText.toString();

        int string_color;
        switch (seekbar_color.getProgress()){
            case 0:
                string_color = Color.RED;
                break;
            case 1:
                string_color = Color.BLUE;
                break;
            case 2:
                string_color = Color.CYAN;
                break;
            case 3:
                string_color = Color.GREEN;
                break;
            case 4:
                string_color = Color.MAGENTA;
                break;
            case 5:
                string_color = Color.WHITE;
                break;
            case 6:
                string_color = Color.YELLOW;
                break;
            default:
                string_color = Color.RED;
                break;
        }

        int viewWidth = bbView.getWidth();
        int viewHeight = bbView.getHeight();

        int string_x = seekbar_x.getProgress();
        int string_y = seekbar_y.getProgress();
        int string_dx = seekbar_dx.getProgress();
        int string_dy = seekbar_dy.getProgress();

        float x = (float) (string_x * viewWidth) / seekbar_x.getMax();
        float y = (float) (string_y * viewWidth) / seekbar_y.getMax();
        float dx = (seekbar_dx.getProgress() / (float)seekbar_dx.getMax());
        float dy = (seekbar_dy.getProgress() / (float)seekbar_dy.getMax());

        Log.d("MainActivity  BUTTON", "Color="+string_color+" X="+string_x+" Y="+string_y+" DX="+string_dx+" DY="+string_dy);

        // let the view do something
        bbView.DawsonButtonPressed(string_color, x, y, dx, dy, extractedNameInput);
    }
}