package com.example.vin.fitnesslifestyleapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class WorkoutInfo extends BaseDrawerActivity {

    private String workoutName;
    private ArrayList<Workout> history = new ArrayList<>();
    private int counter = 0;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_workout_info, frameLayout);

        getIntentData();
        TextView header = (TextView) findViewById(R.id.wName);
        header.setText(workoutName);

        getPrevWorkouts();

        ListView list = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, history);
        list.setAdapter(adapter);

        constructGraph();



    }

    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void getIntentData() {
        if(getIntent().hasExtra("workout_name")) {
            workoutName = getIntent().getStringExtra("workout_name");
        }
    }

    public void getPrevWorkouts() {
        FileInputStream fis;
        String[] workoutInfo;
        try {
            fis = openFileInput("user_data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null) {
                workoutInfo = line.split("-");
                if(workoutInfo[0].equals(workoutName)) {
                    history.add(new Workout("hello", workoutInfo[0], workoutInfo[1], workoutInfo[2], workoutInfo[3]));
                    counter++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void constructGraph() {
        double y;
        int x =counter-1;

        int sets, reps;
        String[] parseSets, parseReps;

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < counter; i++) {
            parseSets = history.get(x).getSets().split(" ");
            parseReps = history.get(x).getReps().split(" ");
            sets = Integer.parseInt(parseSets[0]);
            reps = Integer.parseInt(parseSets[0]);
            y = sets*reps;
            series.appendData(new DataPoint(i,y), true, counter);
            x--;
        }
        graph.addSeries(series);
        graph.setTitle(workoutName + " progress from " + history.get(counter-1).getDate() +
                " to " + history.get(0).getDate());
        GridLabelRenderer grid = graph.getGridLabelRenderer();
        grid.setVerticalAxisTitle("Total Reps Completed");
        grid.setHorizontalAxisTitle("Each Subsequent Workout");

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

    }
}
