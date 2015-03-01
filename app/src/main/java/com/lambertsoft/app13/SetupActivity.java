package com.lambertsoft.app13;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;


public class SetupActivity extends ActionBarActivity {

    Button buttonSetup;
    TextView textCreateUsername, textCreatePassword;
    Client myKinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        buttonSetup = (Button)findViewById(R.id.buttonSetup);
        textCreateUsername = (TextView) findViewById(R.id.textCreateUserName);
        textCreatePassword = (TextView) findViewById(R.id.textCreatePassword);

        myKinveyClient = SplashActivity.myKinveyClient;


        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKinveyClient.user().create(textCreateUsername.getText().toString(), textCreatePassword.getText().toString(), new SetupUserCallback());

            }
        });


    }


    private class SetupUserCallback implements KinveyUserCallback {

        @Override
        public void onFailure(Throwable t) {
            CharSequence text = "Could not sign up.";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(User u) {
            CharSequence text = u.getUsername() + ", your account has been created.";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            myKinveyClient.user().logout().execute();
            finish();
        }
    }


}


