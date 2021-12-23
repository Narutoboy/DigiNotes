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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.do_big.diginotes.R;
import com.do_big.diginotes.adapter.OnNoteItemClickListener;
import com.do_big.diginotes.adapter.SearchAdapter;
import com.do_big.diginotes.databinding.ActivityContentMainBinding;
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.model.NoteViewModel;
import com.do_big.diginotes.utils.AppConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , OnNoteItemClickListener {


    private final int REQ_CODE_SPEECH_INPUT = 100;

    private SearchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NoteViewModel viewModel;
    private ActivityContentMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right, R.anim.fadeout);
        binding= ActivityContentMainBinding.inflate(getLayoutInflater());
        View layoutView  = binding.getRoot();
        setContentView(layoutView);
        viewModel=new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(NoteViewModel.class);
        binding.appBarContentMain.contentMain.myRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        binding.appBarContentMain.contentMain.myRecyclerView.setLayoutManager(mLayoutManager);

        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //TODO chek activity can't convert to onitem click listener
                mAdapter = new SearchAdapter(notes,  MainActivity.this);
                binding.appBarContentMain.contentMain.myRecyclerView.setAdapter(mAdapter);
            }
        });
        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNotesFragment fragment= new AddNotesFragment();
                fragment.show(getSupportFragmentManager(),"add");
                Snackbar.make(binding.getRoot(),"add notes", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            //etMultiline.setText(sharedText);
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
/*
        inputManager.hideSoftInputFromWindow(etKeyword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
*/

        int id = view.getId();


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
                   // etMultiline.setText(result.get(0));
                }
                break;
            }

        }
    }


    @Override
    public void onNoteItemClick(int adapterPosition, Note note, int viewId) {

    }
}
