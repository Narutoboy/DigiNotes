package com.do_big.diginotes.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.do_big.diginotes.data.NoteRepository;

import java.util.List;

public class NoteViewModel  extends AndroidViewModel {

    public static NoteRepository respository;

    public static LiveData<List<Note>> allNotes;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        respository= new NoteRepository(application);
        allNotes=respository.getAllNotes();
    }
    public static LiveData<List<Note>> getAllNotes(){return allNotes;}
    public static  void insert(Note note){
        respository.insert(note);
    }
    public static LiveData<Note> get(long id){return respository.get(id);}
    public static void update(Note note){respository.update(note);}
    public static void delete(Note note){
        respository.delete(note);
    }



}
