package com.do_big.diginotes.model;


import androidx.annotation.ColorInt;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName ="notes_table")
public class Note {
    @ColumnInfo(name ="id")
    @PrimaryKey(autoGenerate = true)
    public long noteId;
    @ColumnInfo(name = "note_description")
    public String NoteDescription;
    @ColumnInfo(name = "note_title")
    public String noteTitle;
    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "is_done")
    public boolean isDone;
    @ColumnInfo(name ="modified_at")
    public Date modifiedAt;

    public Note(String noteDescription, String noteTitle, Date createdAt, boolean isDone, Date modifiedAt) {
        NoteDescription = noteDescription;
        this.noteTitle = noteTitle;
        this.createdAt = createdAt;
        this.isDone = isDone;
        this.modifiedAt = modifiedAt;
    }

    public String getNoteDescription() {
        return NoteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        NoteDescription = noteDescription;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", NoteDescription='" + NoteDescription + '\'' +
                ", noteTitle='" + noteTitle + '\'' +
                ", createdAt=" + createdAt +
                ", isDone=" + isDone +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
