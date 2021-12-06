package com.do_big.diginotes.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.do_big.diginotes.R;
import com.do_big.diginotes.utils.AppConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageButton mic;
    private EditText etMultiline;
    private EditText etKeyword;
    private TextView btnDate;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right, R.anim.fadeout);
        setContentView(R.layout.activity_content_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mic = findViewById(R.id.btnmic);
        Button btnSave = findViewById(R.id.btnSave);
        etMultiline = findViewById(R.id.descript);
        etKeyword = findViewById(R.id.etKeyword);
        btnDate = findViewById(R.id.btnDate);
        btnDate.setText("Date:-  " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        setupDB();
        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            etMultiline.setText(sharedText);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder b1 = new AlertDialog.Builder(MainActivity.this);
            b1.setTitle("Exit");
            b1.setMessage("Close Digi-Note");
            b1.setPositiveButton("No", (arg0, arg1) -> arg0.cancel());
            b1.setNegativeButton("Yes", (arg0, arg1) -> MainActivity.this.finish());
            overridePendingTransition(R.anim.right, R.anim.fadeout);
            b1.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add) {
        } else if (id == R.id.nav_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_edit) {
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        } else if (id == R.id.nav_delete) {
            Toast.makeText(this, "Long press ITEM for delete IT", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Search.class));
        } else if (id == R.id.rate_us) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.PLAY_STORE_PATH)));
            overridePendingTransition(R.anim.right, R.anim.fadeout);
            return true;
        } else if (id == R.id.nav_share) {


            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "DigiNotes : https://play.google.com/store/apps/details?id=com.do_big.diginotes");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            overridePendingTransition(R.anim.right, R.anim.fadeout);
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view) {
        InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(etKeyword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        int id = view.getId();

        switch (id) {
            case R.id.btnSave:

                if (TextUtils.isEmpty(etMultiline.getText().toString())) {
                    etMultiline.setError("Description cannot be null");
                } else if (TextUtils.isEmpty(etKeyword.getText().toString())) {
                    etKeyword.setError("Title cannot be null");
                }/*else if (etKeyword.getText().toString().contains(" ")||etKeyword.getText().toString().contains(".")) {
                    etKeyword.setError("there should  be no space , replace space with comma");
                }*/ else {
                    insert();
                    etMultiline.setText(null);
                    etKeyword.setText("");

                }
                break;
          /*  case R.id.btnSearch:
                if (TextUtils.isEmpty(etKeyword.getText().toString())) {
                etKeyword.setError("Title cannot be null");
            }
                Intent searchIntent = new Intent(MainActivity.this, Search.class);
                searchIntent.putExtra("search", etKeyword.getText().toString().trim());
                startActivity(searchIntent);
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
*/
            case R.id.btnmic:
                promptSpeechInput();
                break;
            case R.id.btnDate:
                final Dialog setDate = new Dialog(MainActivity.this);
                setDate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                setDate.setContentView(R.layout.date);
                setDate.show();
                DatePicker date = setDate.findViewById(R.id.datePicker);
                // date.setMinDate(System.currentTimeMillis() - 1000);
                Calendar calender = Calendar.getInstance();

                DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        year = years;
                        month = monthOfYear;
                        setDate.dismiss();
                        btnDate.setText(day + "/" + (month + 1) + "/" + year);

                    }
                };

                day = calender.get(Calendar.DAY_OF_MONTH);
                month = calender.get(Calendar.MONTH);
                year = calender.get(Calendar.YEAR);

                date.init(year, month, day, onDateChangedListener);


              /*  Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
                datePickerDialog  = new DatePickerDialog(getApplication(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofyear, int dayOfMonth) {
                        monthofyear++;
                        btnDate.setText(dayOfMonth + "/" + monthofyear + "/" + year);
                    }
                }, day, month, year);
               
                datePickerDialog.show();
*/
                break;

        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etMultiline.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void insert() {
        SQLiteDatabase db = openOrCreateDatabase(AppConstant.DATABASE_NAME, MODE_PRIVATE, null);
        String sql = "insert into gtable(description , keyword, date) values(? , ?, ?)";

        Object[] oa = new Object[3];
        oa[0] = etMultiline.getText().toString().trim();
        oa[1] = etKeyword.getText().toString().trim() + btnDate.getText().toString().trim();
        oa[2] = btnDate.getText().toString().trim();
        db.execSQL(sql, oa);
        db.close();
        Toast.makeText(MainActivity.this, "Done!!!", Toast.LENGTH_SHORT).show();

    }

    private void setupDB() {
        //deleteDatabase("mydb");
        SQLiteDatabase db = openOrCreateDatabase(AppConstant.DATABASE_NAME, MODE_PRIVATE, null);
        String sql = "create table if not exists gtable " + " (id INTEGER  PRIMARY KEY, description VARCHAR  NOT NULL  UNIQUE , keyword VARCHAR[50]  unique ,date VARCHAR)";
        db.execSQL(sql);
        db.close();
    }

}
