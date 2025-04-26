package com.do_big.diginotes.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.do_big.diginotes.data.NoteDao
import com.do_big.diginotes.model.Note
import com.do_big.diginotes.utils.AppConstant.DATABASE_NAME
import com.do_big.diginotes.utils.AppConstant.DB_VERSION
import java.util.Calendar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(exportSchema = true, version = DB_VERSION, entities = [Note::class])
@TypeConverters(*[Converters::class])
abstract class NoteRoomDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        const val NUMBERS_OF_THREAD: Int = 4


        @JvmField
        val databaseWriterExecutor: ExecutorService = Executors.newFixedThreadPool(
            NUMBERS_OF_THREAD
        )

        @Volatile
        var INSTANCE: NoteRoomDataBase? = null
        private val sRoomDataBaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriterExecutor.execute {
                    val noteDao = INSTANCE!!.noteDao()
                    noteDao.deleteAll()
                    val etNote = """
                        Some of Digi Note feature :-
                        1. Add note either via share or write or Voice .
                        2. Search notes (Title or Date ) . 
                        3. Having Setting so it will easy to focus on learning .
                        4. Edit TextSize and Night Mode .
                        5. Stay with us lot of New feature and on the way .
                        Happy Learning :)
                        """.trimIndent()
                    val title = "How to Use "

                    val createdAt = Calendar.getInstance().time
                    val howToUseNote =
                        Note(
                            noteDescription = etNote,
                            noteTitle = title,
                            createdAt = createdAt,
                            isFav = true,
                            modifiedAt = null
                        )
                    noteDao.insertNote(howToUseNote)
                }
            }
        }

        @JvmStatic
        fun getINSTANCE(context: Context): NoteRoomDataBase? {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder<NoteRoomDataBase>(
                    context.applicationContext,
                    NoteRoomDataBase::class.java, DATABASE_NAME
                )
                    .addCallback(sRoomDataBaseCallback)
                    .build()
            }

            return INSTANCE
        }
    }
}
