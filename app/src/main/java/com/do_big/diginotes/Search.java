package com.do_big.diginotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.LinkedList;

public class Search extends AppCompatActivity {
    private EditText etsearch;
    private ListView listview;
    private String data;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        overridePendingTransition(R.anim.right,R.anim.fadeout);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9084411889674439/1879858158");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
       // String data = getIntent().getExtras().getString("search");
        etsearch = (EditText) findViewById(R.id.etSearch);
     //   etsearch.setText(data);
        listview = (ListView) findViewById(R.id.list);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv1 = (TextView) view;
                String s1 = tv1.getText().toString();
                final String name = s1.substring(0, s1.indexOf(" :: "));
                AlertDialog.Builder ab1 = new AlertDialog.Builder(Search.this);
                ab1.setTitle("Delete!!");
                ab1.setMessage("Are you sure you want to delete/update " + name);
                ab1.setPositiveButton("Delete ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(name);
                    }
                });
                ab1.setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      Intent share=new Intent(Search.this,ContentMain.class);
                        share.setAction(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT,
                               show(name) );
                        share.setType("text/plain");
                        startActivity(share);
                        delete(name);
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }

                    }
                });
                ab1.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                ab1.show();

                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tv1 = (TextView) view;
                String s1 = tv1.getText().toString();
                final String name = s1.substring(0, s1.indexOf(" :: "));
                              Intent detailintent = new Intent(Search.this, Description.class);
                detailintent.putExtra("des", name);
                startActivity(detailintent);

            }
        });
        populateList();
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
    }
    private String show(String name) {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "select * from gtable where keyword like ? ";
        String oa[] = new String[1];
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
    private void delete(String name) {
        SQLiteDatabase db = openOrCreateDatabase("diginotes", MODE_PRIVATE, null);
        String sql = "delete from gtable where keyword = ? ";
        Object oa[] = new Object[1];
        oa[0] = name;
        db.execSQL(sql, oa);
        db.close();
        populateList();
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        // Toast.makeText(Main2Activity.this, "Done", Toast.LENGTH_SHORT).show();
        // populateList();
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
        LinkedList<String> res = new LinkedList<>();
        while (c1.moveToNext()) {
            String name = c1.getString(in1);
            String date = c1.getString(in2);
            res.add(name + " :: " + date);
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, res);
        listview.setAdapter(aa);
        db.close();

}
}