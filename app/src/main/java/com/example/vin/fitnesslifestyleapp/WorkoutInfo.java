package com.example.vin.fitnesslifestyleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class WorkoutInfo extends AppCompatActivity {

    private String workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_info);

        getIntentData();
        TextView header = (TextView) findViewById(R.id.wName);
        header.setText(workoutName);
    }

    public void getIntentData() {
        if(getIntent().hasExtra("workout_name")) {
            workoutName = getIntent().getStringExtra("workout_name");
        }
    }
}
