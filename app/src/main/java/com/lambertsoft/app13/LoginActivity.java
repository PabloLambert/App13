package com.lambertsoft.app13;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.Client;


import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class LoginActivity extends ActionBarActivity {

    Button buttonLogin, buttonGoSetup;
    TextView textLoginUsername, textLoginPassword;
    Client myKinveyClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoSetup = (Button) findViewById(R.id.buttonGoSetup);

        textLoginUsername = (TextView) findViewById(R.id.textLoginPassword);
        textLoginPassword = (TextView) findViewById(R.id.textLoginPassword);

        myKinveyClient = SplashActivity.myKinveyClient;

        buttonGoSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
                startActivity(intent);

            }
        });

        buttonLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKinveyClient.user().login(textLoginUsername.getText().toString(), textLoginPassword.getText().toString(), new LoginUserCallback());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

private class LoginUserCallback implements KinveyUserCallback {
    @Override
    public void onFailure(Throwable t) {
        CharSequence text = "Wrong username or password.";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSuccess(User u) {
        CharSequence text = "Welcome back," + u.getUsername() + ".";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        finish();
    }
}

}
