package com.do_big.diginotes.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.do_big.diginotes.R;
import com.do_big.diginotes.databinding.FragmentAddNotesBinding;
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.model.NoteViewModel;
import com.do_big.diginotes.model.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddNotesFragment extends BottomSheetDialogFragment {


private FragmentAddNotesBinding binding;
    private SharedViewModel sharedViewModel;
    private Boolean isEdit= false;

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
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.tvCalendar.append(new SimpleDateFormat("E, dd/MM/yyyy").format(Calendar.getInstance().getTime()) );
       /* binding.btnMic.setOnClickListener(btnMicClick ->{
            Toast.makeText(getActivity(), "mic clicked", Toast.LENGTH_SHORT).show();
            sharedViewModel.setVoiceInput(true);
            dismiss();
            //invoke voice input and return back to fragment

        });*/
        binding.btnSave.setOnClickListener(v -> {
            //for update notes
            String etNote= binding.etNotes.getText().toString();
            if(!TextUtils.isEmpty(etNote)) {
                if (isEdit) {
                    Note updateNote = sharedViewModel.getSelectedItem().getValue();
                    updateNote.setNoteDescription(etNote);
                    updateNote.setModifiedAt(Calendar.getInstance().getTime());
                    NoteViewModel.update(updateNote);
                    sharedViewModel.setEdit(false);
                }
                //for first time
                else  {
                    Date createdAt = Calendar.getInstance().getTime();
                    String title = etNote.substring(0, etNote.indexOf(' '));
                    NoteViewModel.insert(new Note(etNote, title, createdAt, false, null));
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Note Saved", BaseTransientBottomBar.LENGTH_SHORT).show();
                    //binding.etNotes.setText("");
                    dismiss();
                }
            }else {
                binding.etNotes.setError(getString(R.string.error_text_field_cannot_empty));
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue()!=null){
            isEdit=sharedViewModel.getEdit();
            binding.etNotes.setText(""+sharedViewModel.getSelectedItem().getValue().getNoteDescription());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding= null;
    }
}