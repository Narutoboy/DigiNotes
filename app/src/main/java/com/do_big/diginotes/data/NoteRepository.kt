package com.do_big.diginotes.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.do_big.diginotes.model.Note
import com.do_big.diginotes.utils.NoteRoomDataBase
import com.do_big.diginotes.utils.NoteRoomDataBase.Companion.getINSTANCE

class NoteRepository(application: Application) {
    private val noteDao: NoteDao

    @JvmField
    val allNotes: LiveData<List<Note>>

    init {
        val noteRoomDataBase = getINSTANCE(application)
        noteDao = noteRoomDataBase!!.noteDao()
        allNotes = noteDao.notes
    }

    fun insert(note: Note?) {
        NoteRoomDataBase.databaseWriterExecutor.execute { noteDao.insertNote(note) }
    }

    fun get(id: Long): LiveData<Note> {
        return noteDao[id]
    }

    fun update(note: Note?) {
        NoteRoomDataBase.databaseWriterExecutor.execute { noteDao.update(note) }
    }

    fun delete(note: Note?) {
        NoteRoomDataBase.databaseWriterExecutor.execute { noteDao.delete(note) }
    }
}
