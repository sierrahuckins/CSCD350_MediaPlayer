package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Andrew Macy on 11/7/2015.
 * Interface for interacting with database
 * Changed interface name.
 */
public interface ISQLite {
    /*
    // create database
    // this should be automatically because we know what tables we want

    // create database
    void createDatabase(String name);
    // create table in database
    void createTable(String name);
    // set key
    void setPrimaryKey(String table, String key);
    // add column to database
    void add(String table, String title);
    // delete column from database
    void remove(String table, String title);
    */

    // adding database entries
    void populateDatabase(MediaFile[] foundFiles);

    // add song to database
    boolean addMediaFile(MediaFile mediaFile);
    boolean addFileToPlaylist(String playlist, MediaFile song);

    // querying database

    // get media files
    MediaFile getMediaFile(String uri);                  // get single
    MediaFile[] getMediaFiles();                         // get all
    MediaFile[] getMediaFilesFromAlbum(String album);
    MediaFile[] getMediaFilesFromArtist(String artist);
    MediaFile[] getMediaFilesFromPlaylist(String playlist);

    // get list of playlists
    String[] getPlaylists();

    // get list of artists
    String[] getArtists();

    // get list of albums
    String[] getAlbums();

    // get list of songs
    SongData[] getSongs();
    String[] getSongTitles();
    String[] getSongsFromPlaylist(String playlist);

    // get list of albums by an artist
    String[] getAlbums(String artist) ;

    // get list of songs in an album
    SongData[] getSongs(String album);

    // get all the MediaFiles

    /*
     * search the database
     *
     * return an array of MediaFiles based on the search
     * This will search all of the fields, Artist, Album, Title, and Genre
     */
    MediaFile[] search(String search);
}
