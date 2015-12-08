package com.mediaplayer.tba.cscd350_mediaplayer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * LibraryDatabase.java
 * Author: Andrew Macy
 * Date: 20151101
 * Description: Class that creates and accesses Media Database
 */
public class LibraryDatabase extends SQLiteOpenHelper implements ISQLite{
    private static final String DATABASE_NAME = "MediaPlayerLibrary.db";
    private static final String LIBRARY_TABLE = "library";
    private static final String ARTIST = "artist";
    private static final String ALBUM = "album";
    private static final String TITLE = "title";
    private static final String GENRE = "genre";
    private static final String theURI = "uri";
    private static final String PLAYLIST_TABLE = "playlists";
    private static final String PLAYLIST = "playlist";

    public LibraryDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE library (title TEXT NOT NULL, artist TEXT NOT NULL, " +
                "album TEXT NOT NULL, genre TEXT NOT NULL, uri TEXT NOT NULL, " +
                "PRIMARY KEY (uri));");
        db.execSQL("CREATE TABLE playlists (playlist TEXT NOT NULL, uri TEXT NOT NULL, " +
                "FOREIGN KEY(uri) REFERENCES library(uri));");
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
        contentValues.put(TITLE, mediaFile.getTitle());
        contentValues.put(ARTIST, mediaFile.getArtist());
        contentValues.put(ALBUM, mediaFile.getAlbum());
        contentValues.put(GENRE, mediaFile.getGenre());

        if(mediaFile.getUri() != null)
            contentValues.put(theURI, mediaFile.getUri().toString());
        else
            return false;

        boolean result;

        try {
            result = db.insert(LIBRARY_TABLE, null, contentValues) != -1;
        }
        catch(SQLiteConstraintException e){
            return false; //item already in database
        }

        return result;
    }

    @Override
    public boolean addFileToPlaylist(String playlist, MediaFile song) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYLIST, playlist);

        if(song.getUri() != null)
            contentValues.put(theURI, song.getUri().toString());
        else
            return false;

        long result = db.insert(PLAYLIST_TABLE, null, contentValues);

        return result != -1;
    }

    @Override
    public boolean removeFromLibrary(String uri) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String delete = "DELETE FROM " + LIBRARY_TABLE + " ";
            String where = "WHERE " + theURI + " = \"" + uri + "\";";
            db.execSQL(delete + where);
            return true;
        }
        catch(SQLException e) { // If delete fails
            return false;
        }
    }

    @Override
    public boolean removeFromPlaylist(String playlist, String title) {
        try{
            SQLiteDatabase db = getWritableDatabase();
            String delete = "DELETE FROM " + PLAYLIST_TABLE + " ";
//            String where = "WHERE " + PLAYLIST  + " = \"" + playlist + "\" AND " + TITLE + " = \"" + title + "\"";
            // this will remove the first instance of the entry with @title
            String where = "WHERE " + PLAYLIST + " = \"" + playlist + "\" AND " + theURI + " IN (SELECT " + theURI + " FROM " + LIBRARY_TABLE + " WHERE " + TITLE + " = \"" + title + "\")";
            db.execSQL(delete + where);
            return true;
        }
        catch(SQLException e) {// if delete fails
            return false;
        }
    }

    @Override
    public MediaFile getMediaFile(String uri) {
        String[] select = {"*"};
        String where = theURI + " = \"" + uri + "\"";

        Cursor results = queryLibrary(select, where);

        MediaFile item = new MediaFile();

        item.setTitle(results.getString(0));
        item.setArtist(results.getString(1));
        item.setAlbum(results.getString(2));
        item.setGenre(results.getString(3));

        Uri tmpURI = Uri.parse(results.getString(4));
        item.setUri(tmpURI);

        return item;
    }

    @Override
    public String[] getPlaylists() {
        String[] select = {"DISTINCT " + PLAYLIST};
        String where = "";

        Cursor results = queryPlaylist(select, where);

        return constructStringResults(results);
    }

    // get list of artists
    @Override
    public String[] getArtists(){
        String[] select = {"DISTINCT " + ARTIST};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    // get list of albums
    @Override
    public String[] getAlbums(){
        String[] select = {"DISTINCT " + ALBUM};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    @Override
    public String[] getGenres() {
        String[] select = {"DISTINCT " + GENRE};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    // get list of songs
    @Override
    public MediaFile[] getSongs(){
        String[] select = {TITLE, theURI};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public String[] getSongTitles() {
        String[] select = {TITLE};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    @Override
    public String[] getSongsFromPlaylist(String playlist) {
        String[] select = {TITLE, theURI};
        String where = PLAYLIST + " = \"" + playlist + "\"";

        Cursor results = queryPlaylist(select, where);

        return constructStringResults(results);
    }

    // get list of albums by an artist
    @Override
    public String[] getAlbums(String artist){
        String[] select = {"DISTINCT " + ALBUM};
        String where = ARTIST + " = \"" + artist + "\"" ;

        Cursor results = queryLibrary(select, where);

        return constructStringResults(results);
    }

    // get list of songs in an album
    @Override
    public MediaFile[] getSongs(String album){
        String[] select = {"* "};
        String where = ALBUM + " = \"" + album + "\"";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] getMediaFiles() {
        String[] select = {"*"};
        String where = "";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] getMediaFilesFromAlbum(String album) {
        String[] select = {"*"};
        String where = ALBUM + " =  \"" + album + "\"";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] getMediaFilesFromGenre(String genre) {
        String[] select = {"*"};
        String where = GENRE + " =  \"" + genre + "\"";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] getMediaFilesFromArtist(String artist) {
        String[] select = {"*"};
        String where = ARTIST + " = \"" + artist + "\"";

        Cursor results = queryLibrary(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] getMediaFilesFromPlaylist(String playlist) {
        String[] select = {TITLE, ARTIST, ALBUM, GENRE, theURI};
        String where = PLAYLIST + " = \"" + playlist + "\"";

        Cursor results = queryPlaylist(select, where);

        return constructMediaFileResults(results);
    }

    @Override
    public MediaFile[] search(String search) {

        SQLiteDatabase db = this.getWritableDatabase();

        String table = LIBRARY_TABLE;
        String[] columnsToReturn = { "*" };
        String selection = "(\"" + TITLE + "\" || \"" + ARTIST + "\" || \"" + ALBUM + "\" || \"" + GENRE + "\") LIKE ?";
        String[] selectionArgs = { "%" + search + "%" }; // matched to "?" in selection
        Cursor cursor = db.query(table, columnsToReturn, selection, selectionArgs, null, null, null);

        return constructMediaFileResults(cursor);
    }

    private Cursor queryLibrary(String[] select, String where) {
        String selectClause = "select " + buildSelectClause(select) + " ";
        String fromClause = "from " + LIBRARY_TABLE + " ";
        String whereClause = "";

        if(!where.equals(""))   // tests if a where clause is needed
            whereClause = "where " + where;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectClause + fromClause + whereClause, null);
    }

    private Cursor queryPlaylist(String[] select, String where){
        String selectClause = "select " + buildSelectClause(select) + " ";
        String fromClause = "from " + PLAYLIST_TABLE + " natural join " + LIBRARY_TABLE + " ";
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
            clause = TITLE + ", " + theURI;  // returns song info and URI on invalid input

        return clause;
    }

    private String[] constructStringResults(Cursor results){
        String[] data = new String[results.getCount()];

        if(results.getCount() == 0)
            return data;

        int i = 0;
        while(results.moveToNext()){
            data[i] = results.getString(0);
            i ++;
        }

        return data;
    }

    private MediaFile[] constructMediaFileResults(Cursor results){
        MediaFile[] data = new MediaFile[results.getCount()];

        if(results.getCount() == 0)
            return data;

        int i = 0;
        while(results.moveToNext()){
            Uri tmpURI;

            tmpURI = Uri.parse(results.getString(4));

            data[i] = new MediaFile(results.getString(1), results.getString(2), results.getString(0),
                    results.getString(3), tmpURI);
            i ++;
        }
        return data;
    }
}