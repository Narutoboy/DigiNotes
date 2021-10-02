package com.do_big.diginotes.activity;

import static com.do_big.diginotes.utils.AppConstant.DATABASE_NAME;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
                    setupDB();
                    insert();


                    startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                    SplashScreen.this.finish();

                }
            };
            Timer t=new Timer();
            t.schedule(tt, WELCOME_DELAY);

        }
        else{
            TimerTask tt=new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }
            };
            Timer t=new Timer();
            t.schedule(tt, SPLASH_DELAY);


        }



    }

    private void insert() {
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String sql = "insert into gtable(description , keyword, date) values(? , ?, ?)";

        Object[] oa = new Object[3];
        oa[0] = "Some of Digi Note feature :-\n" +
                "1. Add note either via share or write or Voice .\n" +
                "2. Search notes (Title or Date ) . \n" +
                "3. Having Setting so it will easy to focus on learning .\n" +
                "4. Edit TextSize and Night Mode .\n" +
                "5. Stay with us lot of New feature and on the way .\n" +
                "Happy Learning :)";
        oa[1] = "How to Use ";
        oa[2] = "01-01-2018";
        db.execSQL(sql, oa);
        db.close();

    }

    private void setupDB() {
        //deleteDatabase("mydb");
        SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String sql = "create table if not exists gtable " + " (id INTEGER  PRIMARY KEY, description VARCHAR  NOT NULL  UNIQUE , keyword VARCHAR[50]  unique ,date VARCHAR)";
        db.execSQL(sql);
        db.close();
    }


}
