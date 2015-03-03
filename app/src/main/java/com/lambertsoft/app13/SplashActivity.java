package com.lambertsoft.app13;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.AsyncUser;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.auth.Credential;

import java.io.IOException;


public class SplashActivity extends ActionBarActivity {

    public static Client myKinveyClient;
    Button buttonAddStudent;
    TextView textUsername;
    LinearLayout linearLayoutBottom, ll;

    private GoogleMap mMap;
    private final LatLng HOME = new LatLng(-33.4311092,-70.5950772);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);
        textUsername = (TextView) findViewById(R.id.textUserName);
        linearLayoutBottom = (LinearLayout) findViewById(R.id.linearLayoutBottom);

        ll = new LinearLayout(getApplicationContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutBottom.addView(ll);


        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivity(intent);
            }
        });

        myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();

        if (myKinveyClient.user().isUserLoggedIn()) {

            String UserName;
            do {
                try {
                    myKinveyClient.user().retrieveBlocking();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                UserName = myKinveyClient.user().getUsername();

            } while (UserName == null);

        }
    }

    public void fillData() {

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        Marker Home = mMap.addMarker( new MarkerOptions().position(HOME).title("Home"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HOME, 10));

        textUsername.setText("Bienvenido: " + myKinveyClient.user().getUsername());
        String Id = myKinveyClient.user().getId();

        final Students students = new Students();
        Query mQuery = myKinveyClient.query();
        mQuery.equals("id_user", Id);
        AsyncAppData<Students> myStudents = myKinveyClient.appData("Students", Students.class);

        myStudents.get(mQuery, new KinveyListCallback<Students>() {
                    @Override
                    public void onSuccess(Students[] studentsArray) {

                        ll.removeAllViews();

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        for (int i=0; i < studentsArray.length; i++ ) {
                            Button btn = new Button(getApplicationContext());
                            btn.setId(i+1);
                            btn.setText(studentsArray[i].getFirst_name());
                            btn.setLayoutParams(params);
                            btn.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence text = "Hello";
                                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                                }
                            });
                            ll.addView(btn);

                        }

                        //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        CharSequence text = "Could not query students...";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();

        if (myKinveyClient.user().isUserLoggedIn()) {

            fillData();

        } else {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            actionLogout();
            return true;
        }
        if (id == R.id.action_refresh ) {
            fillData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void actionLogout() {

        myKinveyClient.user().logout().execute();
        CharSequence text = "Logout...";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }
}