package com.example.vin.fitnesslifestyleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

public class HomeScreen extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home_screen, frameLayout);

        ImageView logWork = (ImageView)findViewById(R.id.logIMG);
        logWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPage = new Intent(HomeScreen.this, LogWorkouts.class);
                startActivity(addPage);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
