package com.do_big.diginotes.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.do_big.diginotes.R;
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.utils.AppConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Description extends AppCompatActivity {
    private LinearLayout back;
    private String des;
    private TextView content;
    private TextToSpeech tts;
    private String data;
    private int ttsStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right, R.anim.fadeout);
        setContentView(R.layout.activity_description2);
        content = findViewById(R.id.content);
        back = findViewById(R.id.background);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsStatus = status;
                tts.setLanguage(Locale.UK);

            }
        });Note note = getIntent().getParcelableExtra(AppConstant.ITEM_CLICKED_PARCEL);
        content.append(note.getNoteDescription());
        Toolbar toolbar = findViewById(R.id.toolbar);
        if(note.getNoteTitle()!=null){
            toolbar.setTitle(note.getNoteTitle());
        }
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tts.speak(note.getNoteDescription(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    @Override
    protected void onPause() {
        tts.stop();
        finish();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        tts.stop();
        super.onBackPressed();


    }



    @Override
    protected void onResume() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String textsize = settings.getString("TextSize", "18");
        Log.d("textSize", textsize);
        content.setTextSize(Float.parseFloat(textsize));
        boolean nightmode = settings.getBoolean("nightMode", false);
        if (nightmode) {
            content.setTextColor(Color.WHITE);
            back.setBackgroundColor(Color.BLACK);

            // content.append("true");
        } else {
            content.setTextColor(Color.BLACK);
            back.setBackgroundColor(Color.WHITE);
            //content.append("false");
        }
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        content.getText().toString() + "\n\tDigiNote");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(Description.this, SettingsActivity.class));
                return true;
            case R.id.action_edit:
                Intent share = new Intent(Description.this, MainActivity.class);
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, data);
                share.setType("text/plain");
                startActivity(share);

                return true;
                *//*
            case R.id.shareApp:
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,
                        "Digi-Notes Study smart!! https://play.google.com/store/apps/details?id=com.bmiEvaluator");
                shareintent.setType("text/plain");
                startActivity(shareintent);
                return true;*//*
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }
}
