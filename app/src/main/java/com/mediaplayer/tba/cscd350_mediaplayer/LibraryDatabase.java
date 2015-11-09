package com.mediaplayer.tba.cscd350_mediaplayer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Andrew Macy on 11/1/2015.
 * Class that creates and accesses our Media Database
 */
public class LibraryDatabase extends SQLiteOpenHelper implements ISQLite{
    private static final String DATABASE_NAME = "MediaPlayerLibrary.db";
    private static final String TABLE_NAME = "library";
    private static final String SONG = "title";
    private static final String ARTIST = "artist";
    private static final String ALBUM = "album";
    private static final String GENRE = "genre";
    private static final String theURI = "uri";

    public LibraryDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE library (title TEXT NOT NULL, artist TEXT NOT NULL, " +
                "album TEXT NOT NULL, genre TEXT NOT NULL, uri TEXT NOT NULL, " +
                "PRIMARY KEY (title, URI));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists library" );
        onCreate(db);
    }


    @Override
    public void populateDatabase(MediaFile[] foundFiles) {
        for(MediaFile file: foundFiles)
            addMediaFile(file);
    }

    @Override
    public boolean addMediaFile(MediaFile mediaFile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SONG, mediaFile.getTitle());
        contentValues.put(ARTIST, mediaFile.getArtist());
        contentValues.put(ALBUM, mediaFile.getAlbum());
        contentValues.put(GENRE, mediaFile.getGenre());

        if(mediaFile.getUri() != null)
            contentValues.put(theURI, mediaFile.getUri().toString());
        else
            return false;

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    // get list of artists
    @Override
    public String[] getArtists(){
        String[] select = {ARTIST};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    // get list of albums
    @Override
    public String[] getAlbums(){
        String[] select = {ALBUM};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    // get list of songs
    @Override
    public SongData[] getSongs(){
        String[] select = {SONG, theURI};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructSongResults(results);
    }

    // get list of albums by an artist
    @Override
    public String[] getAlbums(String artist){
        String[] select = {ALBUM};
        String where = ARTIST + " = " + artist ;

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    // get list of songs in an album
    @Override
    public SongData[] getSongs(String album){
        String[] select = {SONG, theURI};
        String where = ALBUM + " = " + album ;

        Cursor results = queryLibrary(select, where);

        return constructSongResults(results);
    }

    @Override
    public MediaFile[] getMediaFiles() {
        String[] select = {"*"};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] search(String search) {
        String[] select = {"*"};
        String where = "Concat(title, '', artist, '', album, ' '," +
                " genre) like \"%"+ search +"%\"";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    private Cursor queryLibrary(String[] select, String where) {
        String selectClause = "select " + buildSelectClause(select) + " ";
        String fromClause = "from " + TABLE_NAME + " ";
        String whereClause = "";

        if(!where.equals(""))   // tests if a where clause is needed
            whereClause = "where " + where;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectClause + fromClause + whereClause, null);
    }

    private String buildSelectClause(String[] items) {
        String clause = "";

        if(items.length >= 1) {
            clause += items[0];
            for(int i = 1; i < items.length; i++)
                clause += ", " + items[i];
        }
        else
            clause = SONG + ", " + theURI;  // returns song info and URI on invalid input

        return clause;
    }

    private String[] constructStringResults(Cursor results){
        String[] data = new String[results.getCount()];

        if(results.getCount() == 0)
            return data;

        int i = 0;
        while(results.moveToNext()){
            data[i] = results.getString(0);
        }

        return data;
    }

    private MediaFile[] constructMediaFileResults(Cursor results){
        MediaFile[] data = new MediaFile[results.getCount()];

        if(results.getCount() == 0)
            return data;

        int i = 0;
        while(results.moveToNext()){
            URI tmpURI;

            try {
                tmpURI = new URI(results.getString(4));

                data[i] = new MediaFile(results.getString(0), results.getString(1),
                        results.getString(2),results.getString(3),tmpURI );

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    private SongData[] constructSongResults(Cursor results){
        SongData[] data = new SongData[results.getCount()];

        if(results.getCount() == 0)
            return data;

        int i = 0;
        while(results.moveToNext()){
            data[i] = new SongData(results.getString(0), results.getString(1));
        }

        return data;
    }
}