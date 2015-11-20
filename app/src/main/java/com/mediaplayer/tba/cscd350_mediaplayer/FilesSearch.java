package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
/**
 * Created by Kyle on 11/4/2015.
 *
 * File Crawler for Android OS File System
 * // searches /sdcard/
 */
public class FilesSearch implements FileCrawler {

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
            int genreColumn = cursor.getColumnIndex(MediaStore.Audio.GenresColumns.NAME);
            int uriColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                // read the column data and encode into MediaFile object
                String thisArtist = cursor.getString(artistColumn);
                String thisAlbum = cursor.getString(albumColumn);
                String thisTitle = cursor.getString(titleColumn);
                Uri thisUri = Uri.parse(cursor.getString(uriColumn));

                // TODO: 11/9/2015 figure out how to get the genre out of the cursor
//                String thisGenre = cursor.getString(genreColumn);
                String thisGenre = "Blues";
                /*
                new up genre curosr from the uri to move over the genre table
                grab genres while there are genres, and append them onto a genre string
                 */

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
