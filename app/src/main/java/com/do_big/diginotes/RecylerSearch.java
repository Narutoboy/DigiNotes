package com.do_big.diginotes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.LinkedList;

public class RecylerSearch extends AppCompatActivity {
    LinkedList<String> res;
    private EditText etsearch;
    private String data;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyler_search);
        etsearch = findViewById(R.id.etSearch);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mAdapter = new MyAdapter(res);


        mRecyclerView.setAdapter(mAdapter);

    }

    private void populateList() {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "select * from gtable where keyword like ?  or date like ?";
        String oa[] = new String[1];
        int i = 0;
        oa[i++] = "%" + etsearch.getText().toString().trim() + "%";
        // oa[i++]="%"+et3.getText().toString()+"%";
        Cursor c1 = db.rawQuery(sql, oa);
        int in1 = c1.getColumnIndex("keyword");
        int in2 = c1.getColumnIndex("date");
        res = new LinkedList<>();
        while (c1.moveToNext()) {
            String name = c1.getString(in1);
            String date = c1.getString(in2);
            res.add(name + " :: " + date);
        }
        ArrayAdapter aa = new ArrayAdapter<>(this, R.layout.my_text_view, res);
        // mRecyclerView.setAdapter(aa);
        db.close();

    }
}