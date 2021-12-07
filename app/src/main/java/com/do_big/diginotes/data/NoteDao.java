package com.do_big.diginotes.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.do_big.diginotes.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Query("DELETE FROM notes_table")
    void deleteAll();

    @Query("SELECT * FROM notes_table")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes_table WHERE notes_table.id==:id")
    LiveData<Note>get(long id);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
