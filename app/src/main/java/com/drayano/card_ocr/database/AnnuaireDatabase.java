package com.drayano.card_ocr.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Entreprise.class}, version = 3, exportSchema = false)
public abstract class AnnuaireDatabase extends RoomDatabase
{
    public static final String DB_NAME = "entreprise_db";
    private static AnnuaireDatabase instance;

    public static synchronized AnnuaireDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), AnnuaireDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract EntrepriseDao entrepriseDao();
}
