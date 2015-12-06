package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * ISQLite.java
 * Author: Andrew Macy
 * Date: 20151107
 * Description: Interface for interacting with database
 */

public interface ISQLite {

    // adding database entries
    void populateDatabase(MediaFile[] foundFiles);

    // add song to database
    boolean addMediaFile(MediaFile mediaFile);
    boolean addFileToPlaylist(String playlist, MediaFile song);

    // removing items
    boolean removeFromLibrary(String uri);
    boolean removeFromPlaylist(String playlist, String title);

    // querying database

    // get media files
    MediaFile getMediaFile(String uri);                  // get single
    MediaFile[] getMediaFiles();                         // get all
    MediaFile[] getMediaFilesFromAlbum(String album);
    MediaFile[] getMediaFilesFromArtist(String artist);
    MediaFile[] getMediaFilesFromPlaylist(String playlist);
    MediaFile[] getMediaFilesFromGenre(String genre);

    // get list of strings
    String[] getPlaylists();
    String[] getArtists();
    String[] getAlbums();
    String[] getGenres();

    // get list of songs
    SongData[] getSongs();
    String[] getSongTitles();
    String[] getSongsFromPlaylist(String playlist);
    String[] getAlbums(String artist) ;
    SongData[] getSongs(String album);

    // search the database for (artist + album + title + genre) containing @search
    MediaFile[] search(String search);
}
