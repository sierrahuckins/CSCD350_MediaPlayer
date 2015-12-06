package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Author: Andrew Macy
 * Date: 20151107
 * Description: Wrapper for data which describes a song
 */

public class SongData {
    private String mTitle;
    private String mUri;

    public SongData(String title, String theURI) {
        mTitle = title;
        mUri = theURI;
    }

    public String getTitle() { return mTitle; }

    public void setTitle(String song) { this.mTitle = song; }

    public String setUri() { return mUri; }

    public void getTitle(String theURI) { this.mUri = theURI; }

    @Override
    public String toString() {
        return mTitle + " " + mUri;
    }
}