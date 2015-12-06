package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

/**
 * FileSearch.java
 * Author: Kyle Shermer
 * Date: 20151104
 * Description: File crawler to recursively search android file system
 * Revision 1
 * Rev. Author: Bruce Emehiser
 * Date: 20151109
 * Description: Uses content resolver to search android file system for media files
 * Revision 2
 * Rev. Author: Bruce Emehiser
 * Date: 20151203
 * Description: Uses AsyncTask to remove gui lag
 */
public class FileSearch extends AsyncTask<Void, Void, Void> {

    Context mContext;

    // column ids
    private static String[] mediaProjection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
    };

    private static String[] genresProjection = {
            MediaStore.Audio.Genres.NAME,
            MediaStore.Audio.Genres._ID
    };

    public FileSearch(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... objects) {

        if (mContext == null)
            throw new NullPointerException("Context cannot be null");

        // library database to write to
        LibraryDatabase database = new LibraryDatabase(mContext);

        // media file databased maintained by the system
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // get a new cursor from the content resolver, of the specified fields
        Cursor mediaCursor = contentResolver.query(uri, mediaProjection, null, null, null);

        if (mediaCursor == null) {
            // problem with getting the cursor
            return null;
        } else if (!mediaCursor.moveToFirst()) {
            Log.e("File Search", "media cursor is empty");
        } else {
            // get the column ids for all the necessary columns
            int idColumnIndex = mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int titleColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int uriColumn = mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                // read the column data and encode into MediaFile object
                int thisID = Integer.parseInt(mediaCursor.getString(idColumnIndex));
                String thisArtist = mediaCursor.getString(artistColumn);
                String thisAlbum = mediaCursor.getString(albumColumn);
                String thisTitle = mediaCursor.getString(titleColumn);
                Uri thisUri = Uri.parse(mediaCursor.getString(uriColumn));
                String thisGenre = "Unknown";

                // get genre
                Uri genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external", thisID);
                Cursor genreCursor = mContext.getContentResolver().query(genreUri, genresProjection, null, null, null);
                if (genreCursor != null) {
                    // get genre column
                    int genreColumn = genreCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);

                    // get the first genre from cursor. (ignore the rest. You can change with a do while loop, while cursor.moveToNext()
                    if (genreCursor.moveToFirst()) {
                        thisGenre = genreCursor.getString(genreColumn);
                    }
                    genreCursor.close();
                }

                // create new media file with the data
                MediaFile mediaFile = new MediaFile(thisArtist, thisAlbum, thisTitle, thisGenre, thisUri);

                // add media file to the database
                database.addMediaFile(mediaFile);

            } while (mediaCursor.moveToNext());
        }
        mediaCursor.close();

        // return Void
        return null;
    }
}
