package com.do_big.diginotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.do_big.diginotes.R;
import com.do_big.diginotes.adapter.OnNoteItemClickListener;
import com.do_big.diginotes.adapter.SearchAdapter;
import com.do_big.diginotes.data.Titles;
import com.do_big.diginotes.databinding.ActivityRecylerSearchBinding;
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.model.NoteViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnNoteItemClickListener {
    private SearchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NoteViewModel viewModel;
    private ActivityRecylerSearchBinding searchBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = ActivityRecylerSearchBinding.inflate(getLayoutInflater());
        View searchView= searchBinding.getRoot();
        setContentView(searchView);

        viewModel=new ViewModelProvider.AndroidViewModelFactory(SearchActivity.this.getApplication()).create(NoteViewModel.class);
        searchBinding.myRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        searchBinding.myRecyclerView.setLayoutManager(mLayoutManager);

        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //TODO chek activity can't convert to onitem click listener
               mAdapter = new SearchAdapter(notes,  SearchActivity.this);
                searchBinding.myRecyclerView.setAdapter(mAdapter);
            }
        });
        searchBinding.fabAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNotesFragment fragment= new AddNotesFragment();
                fragment.show(getSupportFragmentManager(),"add");
                Snackbar.make(searchBinding.getRoot(),"add notes", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        searchBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //viewModel.get
            }
        });


    }



    @Override
    public void onNoteItemClick(int adapterPosition, Note note, int viewId) {


    }




}