package com.example.vin.fitnesslifestyleapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private int duplicateCounter;


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
        boolean check = checkDuplicate(name);
        if(check) {
            Path path = Paths.get("/data/user/0/com.example.vin.fitnesslifestyleapp/files/user_data.txt");
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                lines.add(duplicateCounter, name + "-" + date + "-" + sets + "-" + reps);
                Files.write(path, lines, StandardCharsets.UTF_8);
                loadWorkouts();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            workoutList.add(new Workout("hello", name, date, sets, reps));
            saveWorkout();
        }
        /*
        Log.i("Has Duplicate", Boolean.toString(check));
        Log.i("Line", Integer.toString(duplicateCounter));
        */
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
            String line, nameBefore = "";

            while((line = br.readLine()) != null) {
                workoutInfo = line.split("-");
                if(!(nameBefore.equals(workoutInfo[0]))) {
                    nameBefore = workoutInfo[0];
                    Workout holder = findWorkout(workoutInfo[0]);
                    if(holder == null) {
                        workoutList.add(new Workout("hello", workoutInfo[0], workoutInfo[1], workoutInfo[2], workoutInfo[3]));
                    }
                    else {
                        Log.i("updating list", "updated");
                        holder.setDate(workoutInfo[1]);
                        holder.setSets(workoutInfo[2]);
                        holder.setReps(workoutInfo[3]);
                        startActivity(new Intent(LogWorkouts.this, LogWorkouts.class));
                    }
                }
                else if(nameBefore.equals(workoutInfo[0])) {
                    nameBefore = workoutInfo[0];
                }

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

    public Workout findWorkout(String name) {
        for(int i = 0; i < workoutList.size(); i++) {
            if((workoutList.get(i).getName()).equals(name)) {
                Log.i("Position", Integer.toString(i));
                return workoutList.get(i);
            }
        }
        return null;
    }

    public boolean checkDuplicate(String wName) {
        FileInputStream fis = null;
        String[] workoutInfo;
        duplicateCounter = 0;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null) {
                workoutInfo = line.split("-");
                if(workoutInfo[0].equals(wName)) {
                    return true;
                }
                duplicateCounter++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
