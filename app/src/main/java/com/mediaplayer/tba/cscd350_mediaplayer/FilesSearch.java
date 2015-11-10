package com.mediaplayer.tba.cscd350_mediaplayer;

import java.io.File;
import java.net.URI;
import java.util.*;
/**
 * Created by Kyle on 11/4/2015.
 *
 * File Crawler for Android OS File System
 * // searches /sdcard/
 */
public class FilesSearch implements FileCrawler
{
    private String filesm="";

//scanFileSystem(Method)
    /*
       *start with extension
       

     */
    @Override
    public URI[] scanFileSystem(String extension) {

        File file = new File(extension);
        int i = 0;
        URI[] uri = new URI[i];

        File[] files = file.listFiles();
        for(File fil : files)
        {
            if(fil.isFile())
            {
                filesm = fil.getName();
                uri[i] = (URI.create(filesm));
                i++;
            }
        }
        return uri;

    }
}
