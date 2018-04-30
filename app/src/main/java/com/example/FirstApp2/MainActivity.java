package com.example.FirstApp2;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String EXTRA_MESSAGE;
    ImageView img1;
    ImageView img2;
    ImageView img3;

    String text[];
    String speechText[];
    String videoIds[];

    protected BottomNavigationView navigationView;

    // ==================================================================
    public static final String TAG = "StepCounter";
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private static final String mClient = "680880859524-vccqua30ninjkk48s0npph935vh6h3ac.apps.googleusercontent.com";
    // ==================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        // ============================================================================
        // Create a FitnessOptions instance, declaring the Fit API data types and access required by your app
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }
        //==============================================================================

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }

    private void accessGoogleFit() {
        //To request background collection of sensor data in your app, use the RecordingClient.subscribe method
        // Google Fit will store fitness data of type TYPE_STEP_COUNT_CUMULATIVE in the fitness history on behalf of your app.
        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Successfully subscribed!");
                                } else {
                                    Log.w(TAG, "There was a problem subscribing.", task.getException());
                                }
                            }
                        });
    }


    private void readData() {
        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                Log.i(TAG, "Total steps: " + total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.read_data){
            readData();
            return true;
        }
        else if(id == R.id.disconnect_from_google_fit){
            disconnectGoogleFit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void disconnectGoogleFit(){
        Fitness.getConfigClient(this, GoogleSignIn.getLastSignedInAccount(this)).disableFit();
    }
    public void onImageClick(View view){
        Intent intent;
        String message;
        switch(view.getId()){
            case R.id.imageView1:
                    intent =new Intent(this, DisplayMessageActivity.class);
                    message="THE STARTUP PLAN";
                    speechText =new String[] {
                        "Get Ready Dynamic Runner Lunges for 60 Seconds",
                        "Up Next Lateral Hip Openers for 60 seconds",
                        "Get Ready Lateral Hip Openers for 60 seconds"
                    };
                    videoIds = new String[]{
                        "7amGIIJJlsI",
                        "9E-1ilf4xlQ",
                        "SOee8Lgzxus"
                        };
                    text= new String[]{
                            "Dynamic Runner Lunges",
                            "Lateral Hip Openers",
                            "Lateral Hip Openers"
                    };
                    intent.putExtra(EXTRA_MESSAGE,message);
                    intent.putExtra("VideoIds",videoIds);
                    intent.putExtra("speechText",speechText);
                    intent.putExtra("text",text);
                    startActivity(intent);
                break;
            case R.id.imageView2:
                 intent =new Intent(this, DisplayMessageActivity.class);
                speechText =new String[] {
                        "Jumping Jacks 20 reps Be quick and light",
                        "Body Weight Squats 15 reps Remember to count your reps. give it your best",
                        "Modified Push Ups 10 reps Come On You have got this"
                };
                videoIds = new String[]{
                        "dmYwZH_BNd0",
                        "UQ9A_CahlN8",
                        "I0ZNsZ2ReCw"
                };
                text= new String[]{
                        "Jumping Jacks",
                        "Body Weight Squats",
                        "Modified Push Ups"
                };
                intent.putExtra("VideoIds",videoIds);
                intent.putExtra("speechText",speechText);
                intent.putExtra("text",text);
                startActivity(intent);
                break;
            case R.id.imageView3:
                intent =new Intent(this, DisplayMessageActivity.class);
                speechText =new String[] {
                        "Get Ready Dynamic Runner Lunges for 60 Seconds",
                        "Up Next Lateral Hip Openers for 60 seconds",
                        "Get Ready Lateral Hip Openers for 60 seconds"
                };
                videoIds = new String[]{
                        "7amGIIJJlsI",
                        "9E-1ilf4xlQ",
                        "SOee8Lgzxus"
                };
                text= new String[]{
                        "Dynamic Runner Lunges",
                        "Lateral Hip Openers",
                        "Lateral Hip Openers"
                };
                intent.putExtra("VideoIds",videoIds);
                intent.putExtra("speechText",speechText);
                intent.putExtra("text",text);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_nutrition:
                Toast.makeText(this, "Nutrition", Toast.LENGTH_SHORT).show();
                intent = new Intent(this,Nutrition.class);
                startActivity(intent);
                return true;
            case R.id.navigation_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                intent = new Intent(this,ProfileActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
