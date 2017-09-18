package com.do_big.diginotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(R.anim.left,R.anim.fadeout);
        TextView title = (TextView) findViewById(R.id.textView2);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation= AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fadein);
        Animation animimage= AnimationUtils.loadAnimation(SplashScreen.this, R.anim.rotate);
        title.startAnimation(animation);
        image.startAnimation(animimage);
        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            TimerTask tt=new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this,HowitWork.class));
                    SplashScreen.this.finish();

                }
            };
            Timer t=new Timer();
            t.schedule(tt,1000);

        }
        else{
            TimerTask tt=new TimerTask() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashScreen.this,ContentMain.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }
            };
            Timer t=new Timer();
            t.schedule(tt,2500);


        }

        //start here




        }


}
