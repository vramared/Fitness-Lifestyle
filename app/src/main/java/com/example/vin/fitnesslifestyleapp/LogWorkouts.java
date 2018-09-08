package com.example.vin.fitnesslifestyleapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;

public class LogWorkouts extends AppCompatActivity {

    private String name, reps, sets, date;

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;

    private List<Workout> workoutList;

    private boolean parseReps, parseSets;
    private Toast errorToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workouts);

        workoutList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WorkoutAdapter(this, workoutList);
        recyclerView.setAdapter(adapter);


        openDialog();

    }

    public void openDialog() {
        FloatingActionButton addWork = (FloatingActionButton)findViewById(R.id.fab);
        addWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = LayoutInflater.from(LogWorkouts.this).inflate(R.layout.activity_add_workout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(LogWorkouts.this);
                builder.setMessage("Enter Workout Information").setCancelable(true).setView(dialogView)
                .setPositiveButton("Add Workout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}})
                .setNegativeButton("Cancel", null);

                final EditText edit_workout_name = (EditText) dialogView.findViewById(R.id.edit_wName);
                final EditText edit_reps = (EditText) dialogView.findViewById(R.id.edit_reps);
                final EditText edit_sets  = (EditText) dialogView.findViewById(R.id.edit_sets);
                final EditText edit_date = (EditText) dialogView.findViewById(R.id.edit_wDate);

                SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" );
                edit_date.setText(sdf.format(new Date()));

                final AlertDialog alert = builder.create();
                alert.setTitle("Record a Workout");
                alert.show();


                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        name = edit_workout_name.getText().toString();
                        reps = edit_reps.getText().toString();
                        sets = edit_sets.getText().toString();
                        date = edit_date.getText().toString();

                        parseReps = isInteger(reps);
                        parseSets = isInteger(sets);

                        Context context = getApplicationContext();
                        String errorMSG;
                        int duration = Toast.LENGTH_SHORT;

                        if(name.equals("") || reps.equals("") || sets.equals("") || date.equals("")) {
                            errorMSG = "Please fill out all categories";
                            errorToast = Toast.makeText(context, errorMSG, duration);
                            errorToast.show();

                        }
                        else if((!parseReps) || (!parseSets)) {
                            errorMSG = "Please enter integers for the Reps and Sets categories.";
                            errorToast = Toast.makeText(context, errorMSG, duration);
                            errorToast.show();
                        }
                        else {
                            alert.dismiss();
                            addCard();
                        }

                    }
                });

            }
        });

    }

    public void addCard() {
        workoutList.add(new Workout("hello", name, date, sets, reps));
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        }
        catch(NumberFormatException e) {
            return false;
        }
        catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
