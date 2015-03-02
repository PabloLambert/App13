package com.lambertsoft.app13;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.java.core.KinveyClientCallback;


public class StudentActivity extends ActionBarActivity {

    TextView textCreateStudentFirstName, textCreateStudentLastName;
    Button buttonCreateStudent;
    Client myKinveyClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        myKinveyClient = SplashActivity.myKinveyClient;

        textCreateStudentFirstName = (TextView) findViewById(R.id.textCreateStudentFirstName);
        textCreateStudentLastName = (TextView) findViewById(R.id.textCreateStudentLastName);
        buttonCreateStudent = (Button)findViewById(R.id.buttonCreateStudent);

        buttonCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Students students = new Students();
                students.setData(textCreateStudentFirstName.getText().toString(), textCreateStudentLastName.getText().toString(), myKinveyClient.user().getId());

                AsyncAppData<Students> myStudents = myKinveyClient.appData("Students", Students.class);
                myStudents.save(students, new KinveyClientCallback<Students>() {
                    @Override
                    public void onSuccess(Students students) {
                        CharSequence text = "Your new student has been created.";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        CharSequence text = "Could not create a student...";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
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
}
