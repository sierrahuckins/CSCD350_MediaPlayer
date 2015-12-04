package com.mediaplayer.tba.cscd350_mediaplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

/**
 * MusicPlayerDialog.java
 * Author: Bruce Emehiser
 * Date: 20151203
 * Description: Dialog boxes for Music Player
 */
public class MusicPlayerDialog {

    public static void addNewPlaylistDialog(final Context context, final LibraryDatabase db, final MediaFile[] toAdd) {

        if(toAdd == null) {
            throw new NullPointerException("toAdd is null in addNewPlaylistDialog");
        }

        // building new dialog for playlist entry
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // set the input type on the builder
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlist = input.getText().toString();
                // add files to playlist
                for (MediaFile m : toAdd) {
                    db.addFileToPlaylist(playlist, m);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // show dialog box
        builder.show();
    }

    public static void selectPlaylistDialog(final Context context, final LibraryDatabase db, final MediaFile[] toAdd) {

        if(toAdd == null) {
            Log.e("TAG", "toAdd is null in selectPlaylistDialog");
            return;
        }
        // build a new dialog box for choosing which playlist to add the item(s) to
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Select Playlist");
        builder.setNeutralButton("New", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // prompt for user input in a new dialog
                addNewPlaylistDialog(context, db, toAdd);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cancel dialog
                dialog.cancel();
            }
        });
        builder.setItems(db.getPlaylists(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get playlist name
                String playlist = db.getPlaylists()[which];
                // add each media file to playlist
                for (MediaFile m : toAdd) {
                    db.addFileToPlaylist(playlist, m);
                }
            }
        });
        // show
        builder.show();
    }

    public static void removePlaylistDialog(final Context context, final LibraryDatabase db, final String playlist) {

        // build a new dialog box for choosing which playlist to add the item(s) to
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete Playlist");
        builder.setMessage("Are you sure you want to delete the playlist?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cancel dialog
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // get list of songs in that playlist
                String[] songs = db.getSongsFromPlaylist(playlist);
                // remove songs from playlist
                for (String s : songs) {
                    db.removeFromPlaylist(playlist, s);
                }
                // dismiss the listener
                dialog.dismiss();
            }
        });
        // show dialog
        builder.show();
    }
}
