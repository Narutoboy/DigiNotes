package com.do_big.diginotes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.do_big.diginotes.model.Note

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: Note)

    @Query("DELETE FROM notes_table")
    fun deleteAll()

    @get:Query("SELECT * FROM notes_table")
    val notes: LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE notes_table.id==:id")
    fun get(id: Long): LiveData<Note>

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}
