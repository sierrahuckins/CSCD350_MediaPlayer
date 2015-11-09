package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 *
 * Interface used to create, read, and write an SQLight database
 */
public interface SQLightInterface {

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

    // add artist to database
    void addArtist(Artist artist);
    // add song to database
    void addMediaFile(MediaFile mediaFile);


    // querying database

    // get list of artists
    String[] getArtists();
    // get list of albums
    String[] getAlbums();
    // get list of songs
    String[] getSongs();

    // get list of albums by an artist
    String[] getAlbums(String artist) ;
    // get list of songs in an album
    String[] getSongs(String album);
    // get the mediaFile information for a given list of songs
    MediaFile getMediaFile(String key);
    // get all the MediaFiles
    MediaFile[] getMediaFiles();

    /*
     * search the database
     *
     * return an array of MediaFiles based on the search
     * This will search all of the fields, Artist, Album, Title, and Genre
     */
    MediaFile[] search(String search);




}
