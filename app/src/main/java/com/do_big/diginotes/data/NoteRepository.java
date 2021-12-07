package com.do_big.diginotes.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.utils.NoteRoomDataBase;

import java.util.List;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteRoomDataBase noteRoomDataBase = NoteRoomDataBase.getINSTANCE(application);
        noteDao = noteRoomDataBase.noteDao();
        allNotes = noteDao.getNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void insert(Note note) {
        NoteRoomDataBase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insertNote(note);
            }
        });
    }

    public LiveData<Note> get(long id) {
        return noteDao.get(id);
    }

    public void update(Note note) {
        NoteRoomDataBase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }

    public void delete(Note note) {
        NoteRoomDataBase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });
    }

}
