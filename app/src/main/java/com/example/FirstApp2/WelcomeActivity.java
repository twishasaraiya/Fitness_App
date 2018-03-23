package com.example.FirstApp2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get firebase instance
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged In
        if(mAuth.getCurrentUser() != null){
            launchHomeScreen();
            finish();
        }

        // make notification bar transparent
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        // layouts of all slides
        layouts = new int[]{
          R.layout.welcome_slide1,
          R.layout.welcome_slide2,
          R.layout.welcome_slide3
        };

        // add bottom dots
        addBottomDots(0);

        // make notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter(layouts,this);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if(current < layouts.length){
                    viewPager.setCurrentItem(current);
                }else{
                    launchHomeScreen();
                }
            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem()+i;
    }

    private void changeStatusBarColor() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();

        for(int i = 0; i< dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dark_grey_dot));
            dotsLayout.addView(dots[i]);
        }

        if(dots.length>0){
            dots[currentPage].setTextColor(Color.WHITE);
        }
    }

    private void launchHomeScreen() {
        if(mAuth.getCurrentUser() != null)
                startActivity(new Intent(this,MainActivity.class));
        else
                startActivity(new Intent(this,SignupActivity.class));
        finish();
    }

    ViewPager.SimpleOnPageChangeListener viewPagerPageChangeListener = new ViewPager.SimpleOnPageChangeListener(){

        @Override
        public void onPageSelected(int position){
            addBottomDots(position);

            if(position == layouts.length-1){
                btnNext.setText("GOT IT");
                btnSkip.setVisibility(View.GONE);
            }else{
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2){

        }

        @Override
        public void onPageScrollStateChanged(int arg0){

        }
    };
}

