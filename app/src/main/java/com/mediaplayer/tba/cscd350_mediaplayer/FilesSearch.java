package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * Created by Kyle on 11/4/2015.
 *
 * File Crawler for Android OS File System
 * // searches /sdcard/
 */
public class FilesSearch implements FileCrawler {

//    @Override
//    public Uri[] scanFileSystem(String extension) {
//
//        File file = new File(extension);
//        int i = 0;
//        URI[] uri = new URI[i];
//
//        File[] files = file.listFiles();
//        for(File fil : files)
//        {
//            if(fil.isFile())
//            {
//                filesm = fil.getName();
//                uri[i] = (URI.create(filesm));
//                i++;
//            }
//        }
//        return uri;
//
//    }

    /**
     * Created by Kyle on 11/4/2015.
     * Updated by Bruce on 11/9/2015 to use ContentResolver and search for all "audio" files
     *
     * File Crawler for Android OS File System
     * // searches /sdcard/
     */
    public MediaFile[] scanFileSystem(Context context) {

        // get content resolver from the context of the main activity
        ContentResolver contentResolver = context.getContentResolver();
        // get the uri of the external storage (usually sdcard or partition mounted with symLink "sdcard")
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // get a new cursor to move through the external entries
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        // array list to contain the entries we select to use
        ArrayList<MediaFile> builderArray = new ArrayList<>();
        Uri externalMediaUri = Uri.parse("content://media/external/audio/media");

        if (cursor == null) {
            // problem with getting the cursor
            return new MediaFile[] {};
        } else if (!cursor.moveToFirst()) {
            // there is no media on the device
        } else {
            // get the column ids for all the necessary columns
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int genreColumn = cursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
//            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int uriColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                // read the column data and encode into MediaFile object
                String thisArtist = cursor.getString(artistColumn);
                String thisAlbum = cursor.getString(albumColumn);
                String thisTitle = cursor.getString(titleColumn);
                // TODO: 11/9/2015 figure out how to get the genre out of the cursor
//                String thisGenre = cursor.getString(genreColumn);
                String thisGenre = "Blues";
//                Uri thisUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, uriColumn);
                Uri thisUri = Uri.parse(cursor.getString(uriColumn));

                // create new media file with the data
                MediaFile mediaFile = new MediaFile(thisArtist, thisAlbum, thisTitle, thisGenre, thisUri);
                // add media file to builder array
                builderArray.add(mediaFile);

            } while (cursor.moveToNext());
        }

        // return array of the contents of the builder
        return builderArray.toArray(new MediaFile[builderArray.size()]);
    }
}
