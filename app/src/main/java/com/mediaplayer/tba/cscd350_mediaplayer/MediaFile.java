package com.mediaplayer.tba.cscd350_mediaplayer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 * Editted Getters on 11/7/2015 to account for null strings - Andrew Macy
 * Wrapper for data which describes a media file.
 */
public class MediaFile implements Parcelable {

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

    // Parcelling part
    public MediaFile (Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        artist = data[0];
        album = data[1];
        title = data[2];
        genre = data[3];
        uri = Uri.parse(data[4]);
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {album,
                artist,
                title,
                genre,
                uri.getPath()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MediaFile createFromParcel(Parcel in) {
            return new MediaFile(in);
        }

        public MediaFile[] newArray(int size) {
            return new MediaFile[size];
        }
    };
}