package com.do_big.diginotes.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

import java.util.Date


@Entity(tableName = "notes_table")
@Parcelize
data class Note(
    @JvmField
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0,

    @ColumnInfo(name = "note_description")
    var noteDescription: String? = null,

    @JvmField
    @ColumnInfo(name = "note_title")
    var noteTitle: String? = null,

    @JvmField
    @ColumnInfo(name = "created_at")
    var createdAt: Date? = null,

    @JvmField
    @ColumnInfo(name = "is_fav")
    var isFav: Boolean = false,

    @JvmField
    @ColumnInfo(name = "modified_at")
    var modifiedAt: Date? = null
) : Parcelable





