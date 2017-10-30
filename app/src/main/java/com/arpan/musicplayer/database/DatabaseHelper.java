package com.arpan.musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "songsDetails.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "songsTable";

    public static final String COMMA = ", ";
    public static final String TEXT = " TEXT";
    public static final String INTEGER = " INTEGER";

    public static final String SONG_URI = "songUri";
    public static final String SONG_NAME = "songName";
    public static final String ALBUM_NAME = "albumName";
    public static final String ALBUM_ID = "albumID";
    public static final String ALBUM_ART_URI = "albumArtUri";
    public static final String ARTIST_NAME = "artistName";
    public static final String SONG_DURATION = "songDuration";

    // String name, String albumName, long albumId, Uri albumArtUri, String artistName, String url, int duration //

    public static final String CREATE_EMPTY_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + INTEGER + " PRIMARY KEY AUTOINCREMENT " + COMMA
            + SONG_NAME + TEXT + COMMA
            + ALBUM_NAME + TEXT + COMMA
            + ALBUM_ID + INTEGER + COMMA //Should be LONG. DO NOT FORGET TO CONVERT.
            + ALBUM_ART_URI + TEXT + COMMA
            + ARTIST_NAME + TEXT + COMMA
            + SONG_URI + TEXT + COMMA
            + SONG_DURATION + TEXT
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPTY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
