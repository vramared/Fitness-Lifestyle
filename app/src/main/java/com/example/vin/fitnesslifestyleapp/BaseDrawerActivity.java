
package com.example.vin.fitnesslifestyleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    View headerView;
    TextView fb_email;
    String email = "test", fb_id;
    TextView fb_name;
    ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_drawer);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //Setup Toolbar

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        fb_email = (TextView) headerView.findViewById(R.id.fb_user_email);
        fb_name = (TextView) headerView.findViewById(R.id.fb_user_name);
        profilePictureView = (ProfilePictureView) headerView.findViewById(R.id.prof_pic);

        if(AccessToken.getCurrentAccessToken() != null) {
            importFB();
        }

    }

    public void importFB() {
        if(AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {

                            if (response.getError() != null) {
                                // handle error
                            }
                            else {
                                email = object.optString("email");
                                fb_email.setText(email);
                                String name = object.optString("name");
                                fb_name.setText(name);
                                fb_id = object.optString("id");
                                profilePictureView.setProfileId(fb_id);
                                Log.i("ID", fb_id);
                            }

                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();

        }
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null) {
            switch(item.getItemId()) {
                case R.id.homeIcon:
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    break;
                case R.id.weightIcon:
                    startActivity(new Intent(getApplicationContext(), LogWorkouts.class));
                    break;
                case R.id.manageUser:
                    LoginManager.getInstance().logOut();
                    finishAffinity();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
                case R.id.clearWorkouts:
                    File dir = getFilesDir();
                    File file = new File(dir, "user_data.txt");
                    boolean deleted = file.delete();
                    startActivity(new Intent(getApplicationContext(), LogWorkouts.class));
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please Login First", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
