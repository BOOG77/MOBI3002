package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action when "Add" button is pressed
        Button addButton = (Button) findViewById(R.id.b_Add);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("M01_Calculator ADD BUTTON", "User tapped the Add button");
                Log.d("M01_Calculator ADD BUTTON", "button =>"+addButton.toString());
                Log.d("M01_Calculator ADD BUTTON", "button =>"+addButton.getText());
                Log.d("M01_Calculator ADD BUTTON", "button =>"+addButton.getId());

                Double d1 = 0.0;
                Double d2 = 0.0;
                Double answer = 0.0;

                EditText textN1 = (EditText) findViewById(R.id.input1);
                EditText textN2 = (EditText) findViewById(R.id.input2);
                // we actually don't need to get ans from screen
                EditText textANS = (EditText) findViewById(R.id.answer);

                try {
                    d1 = Double.parseDouble(textN1.getText().toString());
                    d2 = Double.parseDouble(textN2.getText().toString());
                    answer = d1 + d2;
                }
                catch (Exception e) {
                    Log.w("M01_Calculator ADD BUTTON", "Add Selected with no inputs ... " + answer);
                }

                // Set the Answer into the the answer field
                textANS.setText(answer.toString());

                // log what we are doing
                Log.w("M01_Calculator ADD BUTTON", "Add Selected with => " + d1 + " + " + d2 + "=" + answer);
            }
        });

        Button subtractButton = (Button) findViewById(R.id.b_Subtract);
        subtractButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("M01_Calculator SUBTRACT BUTTON", "User tapped the Add button");
                Log.d("M01_Calculator SUBTRACT BUTTON", "button =>"+subtractButton.toString());
                Log.d("M01_Calculator SUBTRACT BUTTON", "button =>"+subtractButton.getText());
                Log.d("M01_Calculator SUBTRACT BUTTON", "button =>"+subtractButton.getId());

                Double d1 = 0.0;
                Double d2 = 0.0;
                Double answer = 0.0;

                EditText textN1 = (EditText) findViewById(R.id.input1);
                EditText textN2 = (EditText) findViewById(R.id.input2);
                // we actually don't need to get ans from screen
                EditText textANS = (EditText) findViewById(R.id.answer);

                try {
                    d1 = Double.parseDouble(textN1.getText().toString());
                    d2 = Double.parseDouble(textN2.getText().toString());
                    answer = d1 - d2;
                }
                catch (Exception e) {
                    Log.w("M01_Calculator SUBTRACT BUTTON", "Subtract Selected with no inputs ... " + answer);
                }

                // Set the Answer into the the answer field
                textANS.setText(answer.toString());

                // log what we are doing
                Log.w("M01_Calculator SUBTRACT BUTTON", "Subtract Selected with => " + d1 + " - " + d2 + "=" + answer);
            }
        });

        Button multiplyButton = (Button) findViewById(R.id.b_Multiply);
        multiplyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("M01_Calculator MULTIPLY BUTTON", "User tapped the Add button");
                Log.d("M01_Calculator MULTIPLY BUTTON", "button =>"+multiplyButton.toString());
                Log.d("M01_Calculator MULTIPLY BUTTON", "button =>"+multiplyButton.getText());
                Log.d("M01_Calculator MULTIPLY BUTTON", "button =>"+multiplyButton.getId());

                Double d1 = 0.0;
                Double d2 = 0.0;
                Double answer = 0.0;

                EditText textN1 = (EditText) findViewById(R.id.input1);
                EditText textN2 = (EditText) findViewById(R.id.input2);
                // we actually don't need to get ans from screen
                EditText textANS = (EditText) findViewById(R.id.answer);

                try {
                    d1 = Double.parseDouble(textN1.getText().toString());
                    d2 = Double.parseDouble(textN2.getText().toString());
                    answer = d1 * d2;
                }
                catch (Exception e) {
                    Log.w("M01_Calculator MULTIPLY BUTTON", "Add Selected with no inputs ... " + answer);
                }

                // Set the Answer into the the answer field
                textANS.setText(answer.toString());

                // log what we are doing
                Log.w("M01_Calculator MULTIPLY BUTTON", "Multiply Selected with => " + d1 + " * " + d2 + "=" + answer);
            }
        });

        Button divideButton = (Button) findViewById(R.id.b_Divide);
        divideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("M01_Calculator SUBTRACT BUTTON", "User tapped the Add button");
                Log.d("M01_Calculator SUBTRACT BUTTON", "button =>"+divideButton.toString());
                Log.d("M01_Calculator SUBTRACT BUTTON", "button =>"+divideButton.getText());
                Log.d("M01_Calculator SUBTRACT BUTTON", "button =>"+divideButton.getId());

                Double d1 = 0.0;
                Double d2 = 0.0;
                Double answer = 0.0;

                EditText textN1 = (EditText) findViewById(R.id.input1);
                EditText textN2 = (EditText) findViewById(R.id.input2);
                // we actually don't need to get ans from screen
                EditText textANS = (EditText) findViewById(R.id.answer);

                try {
                    d1 = Double.parseDouble(textN1.getText().toString());
                    d2 = Double.parseDouble(textN2.getText().toString());
                    answer = d1 / d2;
                }
                catch (Exception e) {
                    Log.w("M01_Calculator DIVIDE BUTTON", "Divide Selected with no inputs ... " + answer);
                }

                // Set the Answer into the the answer field
                textANS.setText(answer.toString());

                // log what we are doing
                Log.w("M01_Calculator DIVIDE BUTTON", "Divide Selected with => " + d1 + " / " + d2 + "=" + answer);
            }
        });

//        Log.v();


    }

}
