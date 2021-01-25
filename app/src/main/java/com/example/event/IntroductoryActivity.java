package com.example.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.event.OnBoarding.OnBoardingFragment_1;
import com.example.event.OnBoarding.OnBoardingFragment_2;
import com.example.event.OnBoarding.OnBoardingFragment_3;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView logo,appName,splashImg;
    LottieAnimationView lottieAnimationView;

    //OnBoard
    private static final int NUM_PAGES = 3;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    //Anim
    Animation animation;

    private static int SPLASH_TIME_OUT = 1600;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        logo = findViewById(R.id.logo_img_id);
        appName = findViewById(R.id.name_img_id);
        splashImg = findViewById(R.id.bg_img_id);
        lottieAnimationView = findViewById(R.id.lottie);

        //Onboard
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //Anim
        animation = AnimationUtils.loadAnimation(this, R.anim.onboard_anim);
        viewPager.startAnimation(animation);

        splashImg.animate().translationY(-2500).setDuration(1000).setStartDelay(1600);
        logo.animate().translationY(2250).setDuration(1000).setStartDelay(1600);
        appName.animate().translationY(1800).setDuration(1000).setStartDelay(1600);
        lottieAnimationView.animate().translationY(1800).setDuration(1000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime", true);
                if (isFirstTime){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                } else {
                    Intent intent = new Intent(IntroductoryActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnBoardingFragment_1 tab1 = new OnBoardingFragment_1();
                return tab1;
                case 1:
                    OnBoardingFragment_2 tab2 = new OnBoardingFragment_2();
                return tab2;
                case 2:
                    OnBoardingFragment_3 tab3 = new OnBoardingFragment_3();
                return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}