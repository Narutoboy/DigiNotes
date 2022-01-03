package com.do_big.diginotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.do_big.diginotes.R;
import com.do_big.diginotes.utils.PrefManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {


    public static final int SPLASH_DELAY = 2500;
    public static final int WELCOME_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(R.anim.left, R.anim.fadeout);
        TextView title = findViewById(R.id.textView_title);
        Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fadein);
        title.startAnimation(animation);
        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                    SplashScreen.this.finish();

                }
            };
            Timer t = new Timer();
            t.schedule(tt, WELCOME_DELAY);

        } else {
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }
            };
            Timer t = new Timer();
            t.schedule(tt, SPLASH_DELAY);


        }


    }


}
