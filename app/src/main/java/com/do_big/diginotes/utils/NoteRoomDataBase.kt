package com.do_big.diginotes.utils;

import static com.do_big.diginotes.utils.AppConstant.DATABASE_NAME;
import static com.do_big.diginotes.utils.AppConstant.DB_VERSION;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.do_big.diginotes.data.NoteDao;
import com.do_big.diginotes.model.Note;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(exportSchema = true, version = DB_VERSION, entities = {Note.class})
@TypeConverters({Converters.class})

public abstract class NoteRoomDataBase extends RoomDatabase {
    public static final int NUMBERS_OF_THREAD = 4;


    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBERS_OF_THREAD);

    public static volatile NoteRoomDataBase INSTANCE;
    private static final Callback sRoomDataBaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDao noteDao = INSTANCE.noteDao();
                    noteDao.deleteAll();
                    String etNote = "Some of Digi Note feature :-\n" +
                            "1. Add note either via share or write or Voice .\n" +
                            "2. Search notes (Title or Date ) . \n" +
                            "3. Having Setting so it will easy to focus on learning .\n" +
                            "4. Edit TextSize and Night Mode .\n" +
                            "5. Stay with us lot of New feature and on the way .\n" +
                            "Happy Learning :)";
                    String title = "How to Use ";

                    Date createdAt = Calendar.getInstance().getTime();
                    Note howToUseNote =  new Note(etNote, title, createdAt, true, null);

                    noteDao.insertNote(howToUseNote);

                }
            });
        }
    };

    public static NoteRoomDataBase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDataBase.class, DATABASE_NAME)
                    .addCallback(sRoomDataBaseCallback)
                    .build();

        }

        return INSTANCE;
    }

    public abstract NoteDao noteDao();

}
