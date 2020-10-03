package com.example.music_1;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MusicDB {
    public static final String ID = "_id";
    public static final String ID_PROVIDER = "is_provider";
    public static final String TITLE = "song_title";
    public static final String ARTIST = "song_artist";
    public static final String DATA = "song_data";
    public static final String DURATION = "song_duration";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String COUNT_OF_PLAY = "count_of_play";

    public static final String SQLITE_TABLE = "MusicDB";
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    ID + " integer PRIMARY KEY autoincrement," +
                    ID_PROVIDER + "," +
                    TITLE + "," +
                    ARTIST + "," +
                    DURATION + "," +
                    DATA + "," +
                    IS_FAVORITE + "," +
                    COUNT_OF_PLAY + ");" ;
    public static void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}
