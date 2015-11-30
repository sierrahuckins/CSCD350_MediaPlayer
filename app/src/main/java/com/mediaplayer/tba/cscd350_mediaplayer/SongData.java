package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Andrew Macy on 11/7/2015.
 * Simple class for returning a songs title with the URI
 */

public class SongData {
    private String title;
    private String theURI;

    public SongData(String title, String theURI) {
        this.title = title;
        this.theURI = theURI;
    }

    public String getTitle() { return title; }

    public void setTitle(String song) { this.title = song; }

    public String getTheURI() { return theURI; }

    public void setTheURI(String theURI) { this.theURI = theURI; }

    @Override
    public String toString() {
        return title + " " + theURI;
    }
}