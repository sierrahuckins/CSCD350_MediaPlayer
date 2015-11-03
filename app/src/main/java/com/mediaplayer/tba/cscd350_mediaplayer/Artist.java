package com.mediaplayer.tba.cscd350_mediaplayer;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 *
 * Wrapper class to describe the Artists
 */
public class Artist {

    private String name;

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
