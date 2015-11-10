package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 *
 * Scans files and returns a MediaFile array containing all files with the specified extensions
 */
public interface FileCrawler {

    /*
     * scans files system and returns a MediaFile of files
     */
    MediaFile[] scanFileSystem(Context context);
}
