package com.mediaplayer.tba.cscd350_mediaplayer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 * Edited Getters on 11/7/2015 to account for null strings - Andrew Macy
 * Wrapper for data which describes a media file.
 */
public class MediaFile implements Serializable {

    private String artist;
    private String album;
    private String title;
    private String genre;
    private Uri uri;

    public MediaFile(String artist, String album, String title, String genre, Uri uri) {
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

    public Uri getUri() {
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

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return artist + " " + album + " " + title + " " + genre + " " + uri;
    }
}