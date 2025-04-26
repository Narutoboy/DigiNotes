package com.do_big.diginotes.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.do_big.diginotes.R
import com.do_big.diginotes.databinding.FragmentAddNotesBinding
import com.do_big.diginotes.model.Note
import com.do_big.diginotes.model.NoteViewModel
import com.do_big.diginotes.model.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar

class AddNotesFragment : BottomSheetDialogFragment() {
    private var binding: FragmentAddNotesBinding? = null
    private var sharedViewModel: SharedViewModel? = null
    private var isEdit = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding!!.tvCalendar.append(SimpleDateFormat("E, dd/MM/yyyy").format(Calendar.getInstance().time))
        /* binding.btnMic.setOnClickListener(btnMicClick ->{
            Toast.makeText(getActivity(), "mic clicked", Toast.LENGTH_SHORT).show();
            sharedViewModel.setVoiceInput(true);
            dismiss();
            //invoke voice input and return back to fragment

        });*/
        binding!!.btnSave.setOnClickListener { v: View? ->
            //for update notes
            val etNote = binding!!.etNotes.text.toString()
            if (!TextUtils.isEmpty(etNote)) {
                if (isEdit) {
                    val updateNote =
                        sharedViewModel!!.selectedItem.value
                    updateNote!!.noteDescription = etNote
                    updateNote.modifiedAt = Calendar.getInstance().time
                    NoteViewModel.update(updateNote)
                    sharedViewModel!!.edit = false
                } else {
                    val createdAt = Calendar.getInstance().time
                    val title = etNote.substring(0, etNote.indexOf(' '))
                    NoteViewModel.insert(
                        Note(
                            noteDescription = etNote,
                            noteTitle = title,
                            createdAt = createdAt,
                            isFav = false,
                            modifiedAt = null
                        )
                    )
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "Note Saved",
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                    //binding.etNotes.setText("");
                    dismiss()
                }
            } else {
                binding!!.etNotes.error = getString(R.string.error_text_field_cannot_empty)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sharedViewModel!!.selectedItem.value != null) {
            isEdit = sharedViewModel!!.edit
            binding!!.etNotes.setText("" + sharedViewModel!!.selectedItem.value!!.noteDescription)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}