package com.lambertsoft.app13;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.Client;


import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class MainActivity extends ActionBarActivity {

    Button buttonLogin, buttonSetUp;
    TextView textUsername, textPassword;
    Client myKinveyClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSetUp = (Button) findViewById(R.id.buttonSetup);

        textUsername = (TextView) findViewById(R.id.textUserName);
        textPassword = (TextView) findViewById(R.id.textPassword);

        myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();

        buttonSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKinveyClient.user().create(textUsername.getText().toString(), textPassword.getText().toString(), new SetupUserCallback() );
            }
        });

        buttonLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKinveyClient.user().login(textUsername.getText().toString(), textPassword.getText().toString(), new LoginUserCallback());
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
    }
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
        }
}

}
