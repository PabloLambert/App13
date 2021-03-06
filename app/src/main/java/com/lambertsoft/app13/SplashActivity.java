package com.lambertsoft.app13;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;


import java.io.IOException;

public class SplashActivity extends ActionBarActivity {
    final static String TAG = SplashActivity.class.getSimpleName();

    static Client myKinveyClient;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        myKinveyClient = new Client.Builder("kid_WyE5rmap_", "b5f06467ecea486096b5e47104e4e098", getApplicationContext()).build();

        if (myKinveyClient.user().isUserLoggedIn()) {
            fillTabs();
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
            fillTabs();
            return true;
        }
        if (id == R.id.action_user_add ) {
            Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void fillTabs() {

        for ( int count = 0; (myKinveyClient.user().getUsername() == null) && (count < 10); count ++) {

          try {
                myKinveyClient.user().retrieveBlocking();
                Log.v(TAG, "getUsername => null (" + count + ")");
              wait(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String Id = myKinveyClient.user().getId();

        if (Id == null || myKinveyClient.user().getUsername() == null ) {
            Log.e(TAG, "Error in fillTabs");
            return;
        }

        Query mQuery = myKinveyClient.query();
        mQuery.equals("id_user", Id);

        AsyncAppData<Students> myStudents = myKinveyClient.appData("Students", Students.class);

        myStudents.get(mQuery, new KinveyListCallback<Students>() {

            @Override
            public void onSuccess(Students[] studentsArray) {

                //ActionBar
                actionBar.removeAllTabs();

                if (studentsArray != null && studentsArray.length > 0 ) {

                    for (int i = 0; i < studentsArray.length; i++) {

                        ActionBar.Tab tab = actionBar.newTab().setText(studentsArray[i].getFirst_name());

                        tab.setTabListener(new MyTabListener(new FragmentTab()));
                        actionBar.addTab(tab);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                CharSequence text = "Error in query ";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void actionLogout() {

        myKinveyClient.user().logout().execute();
        CharSequence text = "Logout...";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }

}

