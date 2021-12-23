package com.do_big.diginotes.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.do_big.diginotes.R;
import com.do_big.diginotes.data.NoteRepository;
import com.do_big.diginotes.databinding.FragmentAddNotesBinding;
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.model.NoteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;


public class AddNotesFragment extends BottomSheetDialogFragment {


private FragmentAddNotesBinding binding;

    public AddNotesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNotesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSave.setOnClickListener(v -> {

            String etNote= binding.etNotes.getText().toString();
            if(etNote !=null && ! etNote.isEmpty()){
                Date createdAt= Calendar.getInstance().getTime();
                String title =etNote.substring(0, etNote.indexOf(' '));
                NoteViewModel.insert(new Note(etNote,title,createdAt,false,null));
                Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding= null;
    }
}