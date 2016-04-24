package ru.katkalov.android.yamobdev2016.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ArtistDatabaseHelper extends SQLiteOpenHelper {
    private static ArtistDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "artistsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ARTISTS = "artists";

    // Artist Table Columns
    private static final String KEY_ARTIST_ID = "id";
    private static final String KEY_ARTIST_NAME = "name";
    private static final String KEY_ARTIST_GENRES = "genres";
    private static final String KEY_ARTIST_TRACKS = "tracks";
    private static final String KEY_ARTIST_ALBUMS = "almubs";
    private static final String KEY_ARTIST_LINK = "ling";
    private static final String KEY_ARTIST_DESCRIPTION = "description";
    private static final String KEY_ARTIST_SMALL_COVER = "smallCover";
    private static final String KEY_ARTIST_BIG_COVER = "bigCover";


    private ArtistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_ARTISTS +
                "(" +
                KEY_ARTIST_ID + " INTEGER PRIMARY KEY," +
                KEY_ARTIST_NAME + " TEXT," +
                KEY_ARTIST_GENRES + " TEXT," +
                KEY_ARTIST_TRACKS + " INTEGER," +
                KEY_ARTIST_ALBUMS + " INTEGER," +
                KEY_ARTIST_LINK + " TEXT," +
                KEY_ARTIST_DESCRIPTION + " TEXT," +
                KEY_ARTIST_SMALL_COVER + " TEXT," +
                KEY_ARTIST_BIG_COVER + " TEXT" +
                ")";

        db.execSQL(CREATE_POSTS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS);
            onCreate(db);
        }
    }


    public static synchronized ArtistDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ArtistDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public void addArtist(List<Artist> artists) {
        for (Artist artist : artists) {
            addArtist(artist);
        }
    }

    // Insert a post into the database
    public void addArtist(Artist artist) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ARTIST_ID, artist.getId());
            values.put(KEY_ARTIST_NAME, artist.getName());
            values.put(KEY_ARTIST_GENRES, artist.getGenres());
            values.put(KEY_ARTIST_TRACKS, artist.getCountTracks());
            values.put(KEY_ARTIST_ALBUMS, artist.getCountAlbums());
            values.put(KEY_ARTIST_LINK, artist.getLink());
            values.put(KEY_ARTIST_DESCRIPTION, artist.getDescription());
            values.put(KEY_ARTIST_SMALL_COVER, artist.getSmallCover());
            values.put(KEY_ARTIST_BIG_COVER, artist.getBigCover());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_ARTISTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DBERROR", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<Artist> getArtists(int limit, int offset) {
        List<Artist> artists = new ArrayList<>();

        String ARTISTS_SELECT_QUERY =
                String.format("SELECT * FROM %s LIMIT %s OFFSET %s",
                        TABLE_ARTISTS, limit, offset);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ARTISTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    artists.add(new Artist(
                            cursor.getInt(cursor.getColumnIndex(KEY_ARTIST_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_ARTIST_NAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_ARTIST_GENRES)),
                            cursor.getInt(cursor.getColumnIndex(KEY_ARTIST_TRACKS)),
                            cursor.getInt(cursor.getColumnIndex(KEY_ARTIST_ALBUMS)),
                            cursor.getString(cursor.getColumnIndex(KEY_ARTIST_LINK)),
                            cursor.getString(cursor.getColumnIndex(KEY_ARTIST_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(KEY_ARTIST_SMALL_COVER)),
                            cursor.getString(cursor.getColumnIndex(KEY_ARTIST_BIG_COVER))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBERROR", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return artists;
    }


    public boolean isEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        String ARTISTS_COUNT =
                String.format("SELECT count(*) FROM %s",
                        TABLE_ARTISTS);
        Cursor cursor = db.rawQuery(ARTISTS_COUNT, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }


}
