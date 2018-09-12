package com.example.vin.fitnesslifestyleapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogWorkouts extends BaseDrawerActivity {

    private String name, reps, sets, date;

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;

    private List<Workout> workoutList;

    private boolean parseReps, parseSets;
    private Toast errorToast;

    private static final String FILE_NAME = "user_data.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_log_workouts, frameLayout);

        workoutList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WorkoutAdapter(this, workoutList);
        recyclerView.setAdapter(adapter);


        openDialog();
        loadWorkouts();

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

    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    public void addCard() {
        workoutList.add(new Workout("hello", name, date, sets, reps));
        saveWorkout();
    }

    public void saveWorkout() {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            fos.write(name.getBytes());
            fos.write("-".getBytes());
            fos.write(date.getBytes());
            fos.write("-".getBytes());
            fos.write(sets.getBytes());
            fos.write("-".getBytes());
            fos.write(reps.getBytes());
            fos.write("\n".getBytes());
            Log.i("FilePath", LogWorkouts.this.getFilesDir().getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadWorkouts() {
        FileInputStream fis = null;
        String[] workoutInfo;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null) {
                workoutInfo = text.split("-");
                workoutList.add(new Workout("hello", workoutInfo[0], workoutInfo[1], workoutInfo[2], workoutInfo[3]));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
