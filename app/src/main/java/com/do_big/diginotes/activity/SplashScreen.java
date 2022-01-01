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
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.model.NoteViewModel;
import com.do_big.diginotes.utils.PrefManager;

import java.util.Calendar;
import java.util.Date;
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
        //insert How to use in room DB

        String etNote = "Some of Digi Note feature :-\n" +
                "1. Add note either via share or write or Voice .\n" +
                "2. Search notes (Title or Date ) . \n" +
                "3. Having Setting so it will easy to focus on learning .\n" +
                "4. Edit TextSize and Night Mode .\n" +
                "5. Stay with us lot of New feature and on the way .\n" +
                "Happy Learning :)";
        String title = "How to Use ";
        Date createdAt = Calendar.getInstance().getTime();
        NoteViewModel.insert(new Note(etNote, title, createdAt, true, null));
    }


}
