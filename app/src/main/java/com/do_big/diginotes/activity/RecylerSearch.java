package com.do_big.diginotes.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.do_big.diginotes.R;
import com.do_big.diginotes.adapter.MyAdapter;
import com.do_big.diginotes.data.Titles;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class RecylerSearch extends AppCompatActivity implements RecyclerView.OnItemTouchListener {
    ArrayList<Titles> res;
    private EditText etsearch;
    private String data;
    private RecyclerView mRecyclerView;
    private AdView mAdView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyler_search);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        etsearch = findViewById(R.id.etSearch);
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                populateList();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView = findViewById(R.id.my_recycler_view);

        //  mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        populateList();


    }

    private void populateList() {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "select * from gtable where keyword like ?  or date like ?";
        String[] oa = new String[1];
        int i = 0;
        oa[i++] = "%" + etsearch.getText().toString().trim() + "%";
        // oa[i++]="%"+et3.getText().toString()+"%";
        Cursor c1 = db.rawQuery(sql, oa);
        int in1 = c1.getColumnIndex("keyword");
        int in2 = c1.getColumnIndex("date");
        res = new ArrayList<>();
        while (c1.moveToNext()) {
            String name = c1.getString(in1);
            String date = c1.getString(in2);
            res.add(new Titles(name, date));
        }

        MyAdapter myAdapter = new MyAdapter(res, this);
        mRecyclerView.setAdapter(myAdapter);
        db.close();

    }

    private String show(String name) {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "select * from gtable where keyword like ? ";
        String[] oa = new String[1];
        int i = 0;
        oa[i] = "%" + name + "%";
        Cursor c1 = db.rawQuery(sql, oa);
        int in1 = c1.getColumnIndex("description");
        while (c1.moveToNext()) {
            data = c1.getString(in1);
            // Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
        //Intent detailintent=new Intent(Search.this,)


        db.close();
        return data;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}