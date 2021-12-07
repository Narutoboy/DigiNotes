package com.do_big.diginotes.utils;

import static com.do_big.diginotes.utils.AppConstant.DATABASE_NAME;
import static com.do_big.diginotes.utils.AppConstant.DB_VERSION;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.do_big.diginotes.data.NoteDao;
import com.do_big.diginotes.model.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class},version = DB_VERSION, exportSchema = false)
public abstract  class NoteRoomDataBase extends RoomDatabase
{
    public static final int NUMBERS_OF_THREAD=4;


    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBERS_OF_THREAD);

    public static volatile NoteRoomDataBase INSTANCE;
    private static final Callback sRoomDataBaseCallback= new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(new Runnable() {
                @Override
                public void run() {
                     NoteDao noteDao= INSTANCE.noteDao();
                     noteDao.deleteAll();
                }
            });
        }
    };

    public static NoteRoomDataBase getINSTANCE(Context context) {
        if(INSTANCE== null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),NoteRoomDataBase.class, DATABASE_NAME)
                    .addCallback(sRoomDataBaseCallback)
                    .build();

        }

        return INSTANCE;
    }

    public abstract NoteDao noteDao();

}
