package com.example.FirstApp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String EXTRA_MESSAGE;
    ImageView img1;
    ImageView img2;
    ImageView img3;

    String text[];
    String speechText[];
    String videoIds[];

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
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
