package com.do_big.diginotes.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.do_big.diginotes.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Description extends AppCompatActivity {
    LinearLayout back;
    private TextView content;
    private TextToSpeech tts;
    private String data;
    String des;
    private int ttsStatus;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right, R.anim.fadeout);
        setContentView(R.layout.activity_description2);
        content = findViewById(R.id.content);
        back = findViewById(R.id.background);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9084411889674439/1879858158");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsStatus = status;
                tts.setLanguage(Locale.UK);

            }
        });
        des = getIntent().getExtras().getString("des");
        show(des);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
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
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            super.onBackPressed();
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    private void show(String name) {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "select * from gtable where keyword like ? ";
        String[] oa = new String[1];
        int i = 0;
        oa[i] = "%" + name + "%";
        Cursor c1 = db.rawQuery(sql, oa);
        int in1 = c1.getColumnIndex("description");
        while (c1.moveToNext()) {
            data = c1.getString(in1);
            content.setText(data);
            // Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
        //Intent detailintent=new Intent(Search.this,)


        db.close();
    }

    //create menu
    private void delete(String name) {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "delete from gtable where keyword = ? ";
        Object[] oa = new Object[1];
        oa[0] = name;
        db.execSQL(sql, oa);
        db.close();
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        // Toast.makeText(Main2Activity.this, "Done", Toast.LENGTH_SHORT).show();
        // populateList();
    }


    @Override
    protected void onResume() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String textsize = settings.getString("TextSize", "18");
        Log.d("textSize", "" + textsize);
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
        switch (item.getItemId()) {
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
                Intent share = new Intent(Description.this, ContentMain.class);
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, data);
                share.setType("text/plain");
                startActivity(share);
                delete(des);
                return true;
                /*
            case R.id.shareApp:
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,
                        "Digi-Notes Study smart!! https://play.google.com/store/apps/details?id=com.bmiEvaluator");
                shareintent.setType("text/plain");
                startActivity(shareintent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
