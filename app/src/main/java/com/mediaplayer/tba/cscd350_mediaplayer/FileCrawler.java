package com.mediaplayer.tba.cscd350_mediaplayer;

import java.net.URI;

/**
 * Created by Bruce Emehiser on 11/2/2015.
 *
 * Scans files and returns a URI array containing all files with the specified extensions
 */
public interface FileCrawler {


    /*
     * scans files system and returns a URI of files with the specified file type
     */
    URI[] scanFileSystem(String extension);


}
