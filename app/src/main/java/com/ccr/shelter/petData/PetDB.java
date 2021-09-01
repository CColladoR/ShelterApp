package com.ccr.shelter.petData;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Pet.class}, version = 10, exportSchema = false)
public abstract class PetDB extends RoomDatabase {

    public abstract PetDAO petDAO();

    private static volatile PetDB INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PetDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PetDB.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PetDB.class, "pet_database")
                        .addCallback(sRoomDatabaseCallback)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}