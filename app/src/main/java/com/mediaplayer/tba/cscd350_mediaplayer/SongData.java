package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Andrew Macy on 11/7/2015.
 * Simple class for returning a songs title with the URI
 */

public class SongData {
    private String song;
    private String theURI;

    public SongData(String dataRequested, String theURI) {
        this.song = dataRequested;
        this.theURI = theURI;
    }

    public String getDataRequested() { return song; }

    public void setDataRequested(String song) { this.song = song; }

    public String getTheURI() { return theURI; }

    public void setTheURI(String theURI) { this.theURI = theURI; }

    @Override
    public String toString() {
        return song + " " + theURI;
    }
}