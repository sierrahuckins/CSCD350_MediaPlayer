package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Andrew Macy on 11/7/2015.
 * Interface for interacting with database
 * Changed interface name.
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


    /*
     * search the database
     *
     * return an array of MediaFiles based on the search
     * This will search all of the fields, Artist, Album, Title, and Genre
     */
    MediaFile[] search(String search);
}
