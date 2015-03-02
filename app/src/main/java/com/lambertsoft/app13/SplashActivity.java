package com.lambertsoft.app13;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class SplashActivity extends FragmentActivity {

    public static Client myKinveyClient;
    Button buttonLogout, buttonAddStudent;
    TextView textUsername;


    private GoogleMap mMap;
    private final LatLng HOME = new LatLng(-33.4311092,-70.5950772);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);
        textUsername = (TextView) findViewById(R.id.textUserName);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myKinveyClient.user().logout().execute();
                CharSequence text = "Logout...";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivity(intent);
            }
        });

        myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();


        if (myKinveyClient.user().isUserLoggedIn()) {

            //buttonLogout.setEnabled(false);
            fillData();

        } else {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

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
                    public void onSuccess(Students[] studentses) {

                        CharSequence text = "There are: " + students.size() ;
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        CharSequence text = "Could not query students...";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                    }
                });


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}