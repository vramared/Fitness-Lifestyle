package com.example.vin.fitnesslifestyleapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends BaseDrawerActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;

    private String uEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        setTitle("Login");

        facebookLogin();

        accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null) && (!accessToken.isExpired());
        if(isLoggedIn) {
            Intent homePage = new Intent(MainActivity.this, HomeScreen.class);
            startActivity(homePage);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void facebookLogin() {

        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "FB Login Worked", Toast.LENGTH_SHORT).show();
/*
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("Nan", "Binner");
                                try{
                                    //JSONObject jsonObject = (JSONObject) new JSONTokener(uEmail).nextValue();
                                    uEmail = object.getString("email");

                                }
                                catch(JSONException e){}
                            }

                        });
                        */

/*

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
*/
                Intent homePage = new Intent(MainActivity.this, HomeScreen.class);
                startActivity(homePage);


            }


            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "FB Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error with FB Login", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



}