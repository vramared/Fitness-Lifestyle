package com.example.vin.fitnesslifestyleapp;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;


public class MainActivity extends BaseDrawerActivity {


    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        setTitle("Login");

        facebookLogin();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = (accessToken != null) && (!accessToken.isExpired());
        if(isLoggedIn) {
            Intent homePage = new Intent(MainActivity.this, HomeScreen.class);
            startActivity(homePage);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void facebookLogin() {

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "FB Login Worked", Toast.LENGTH_SHORT).show();

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