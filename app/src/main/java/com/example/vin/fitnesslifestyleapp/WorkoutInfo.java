package com.example.vin.fitnesslifestyleapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class WorkoutInfo extends BaseDrawerActivity {

    private String workoutName;
    private ArrayList<Workout> history = new ArrayList<>();
    private int counter = 0;
    LineGraphSeries<DataPoint> series;
    private SwipeMenuListView list;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_workout_info, frameLayout);

        getIntentData();
        setTitle(workoutName + " Info");
        TextView header = (TextView) findViewById(R.id.wName);
        header.setText(workoutName);

        getPrevWorkouts();

        list = (SwipeMenuListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, history);
        list.setAdapter(adapter);

        setupSwipe();

        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.i("Clicked", Integer.toString(position));
                        removeLine(history.get(position));
                        history.remove(position);
                        list.setAdapter(adapter);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

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

    public void removeLine(Workout toBeRemoved) {
        Path path = Paths.get(getApplicationContext().getFilesDir().getPath() + "/user_data.txt");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            Workout holder;
            String[] wValues;
            for(int i = 0; i < lines.size(); i++) {
                wValues = lines.get(i).split("-");
                holder = new Workout(0, wValues[0], wValues[1], wValues[2], wValues[3]);

                if(toBeRemoved.equals(holder)) {
                    Log.i("holder", holder.toString());
                    lines.remove(i);
                    Files.write(path, lines, StandardCharsets.UTF_8);
                    return;

                }
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupSwipe() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(340);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        list.setMenuCreator(creator);
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
                    history.add(new Workout(0, workoutInfo[0], workoutInfo[1], workoutInfo[2], workoutInfo[3]));
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
