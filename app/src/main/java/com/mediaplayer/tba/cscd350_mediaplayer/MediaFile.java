package com.mediaplayer.tba.cscd350_mediaplayer;

import java.net.URI;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 * Editted Getters on 11/7/2015 to account for null strings - Andrew Macy
 * Wrapper for data which describes a media file.
 */
public class MediaFile {

    private String artist;
    private String album;
    private String title;
    private String genre;
    private URI uri;

    public MediaFile(String artist, String album, String title, String genre, URI uri) {
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.genre = genre;
        this.uri = uri;
    }

    public String getArtist() {
        if(artist != null)
            return artist;
        return "";
    }

    public String getAlbum() {
        if(album != null)
            return album;
        return "";
    }

    public String getTitle() {
        if(title != null)
            return title;
        return "";
    }

    public String getGenre() {
        if( genre != null )
            return genre;
        return "";
    }

    public URI getUri() {
        return uri;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
