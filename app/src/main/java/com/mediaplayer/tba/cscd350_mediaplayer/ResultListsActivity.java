package com.mediaplayer.tba.cscd350_mediaplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *ResultsListsActivity.java
 *Author: Sierra Huckins
 *Revision: 1
 *Date: 20151113
 *Rev. Author: Sierra Huckins
 *Description: Activity that allows user to display database contents based on 1 of 5 factors: artists, albums, playlists, genres, and songs.
 *Results from user's choice within the displayed contents are returned to the MainActivity for playing.
 **/
public class ResultListsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    //member variables for buttons and clickable display
    private Button artistsBtn;
    private Button albumsBtn;
    private Button playlistsBtn;
    private Button genreBtn;
    private Button songsBtn;
    private ListView listResults;

    //holds the array list of MediaFiles that will be returned to MainActivity
    private MediaFile[] mediaFiles;

    //holds the current list of strings that is being displayed
    private ArrayList<String> currentList;
    private ArrayList<MediaFile> currentListMediaFiles;

    private enum display {ARTISTS, ALBUMS, PLAYLISTS, SONGS, GENRES}
    private display currentDisplay;

    private ArrayAdapter<String> adapter;
    private LibraryDatabase db = new LibraryDatabase(this);

    // activity intent request response key
    public static final String RESULT_LIST_ACTIVITY_RESPONSE_KEY = "response_key";

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        //initialize references to all buttons
        artistsBtn = (Button)findViewById(R.id.btnArtists);
        albumsBtn = (Button)findViewById(R.id.btnAlbums);
        playlistsBtn = (Button)findViewById(R.id.btnPlaylists);
        genreBtn = (Button)findViewById(R.id.btnGenre);
        songsBtn = (Button)findViewById(R.id.btnSongs);
        listResults = (ListView)findViewById(R.id.listResults);

        //set on click listeners for buttons and clickable display
        artistsBtn.setOnClickListener(this);
        albumsBtn.setOnClickListener(this);
        playlistsBtn.setOnClickListener(this);
        genreBtn.setOnClickListener(this);
        songsBtn.setOnClickListener(this);
        listResults.setOnItemClickListener(this);

        // create current list
        currentList = new ArrayList<>();
        currentListMediaFiles = new ArrayList<>();

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            ArrayList<String> temp = (ArrayList<String>) savedInstanceState.getSerializable("currentList");
            // TODO: 11/22/2015 get currentDisplay as well
            if(temp != null) {
                currentList.addAll(temp);
                // TODO: 11/27/2015 make display persist across app rotations
//                currentDisplay = display.ARTISTS;
            }
        }
        else {
            currentList.addAll(Arrays.asList(db.getArtists()));
            currentDisplay = display.ARTISTS;
        }

        // get new adapter and pass it a list item layout and the text view in the list item
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, currentList);
        // set the adapter on the list in content_resultslistactivity
        listResults.setAdapter(adapter);

        // register as a long click listener. we will show a dialog box
        listResults.setOnItemLongClickListener(this);

        // register as context menu listener
//        registerForContextMenu(listResults);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // get adapter context menu information
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        // get the index of item clicked
        int menuItemIndex = item.getItemId();
        // get the menu items
        String[] menuItems = getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        // get the entry in the list view which was clicked
        String listItemName = currentList.get(info.position);

        Toast.makeText(ResultListsActivity.this, "Item Clicked: " + menuItemName + " current list item: " + listItemName, Toast.LENGTH_SHORT).show();

        // return true, we handled the click
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // if the created context menu is on the list results view
        if (v.getId()==R.id.listResults) {
            // get adapter view context menu information
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            // set menu header to be the current position title
            menu.setHeaderTitle(currentList.get(info.position));
            // menu items as defined by resource array
            String[] menuItems = getResources().getStringArray(R.array.menu);
            // add items to menu
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    //onClick listener for buttons
    @Override
    public void onClick(View v) {

        String[] temp = new String[0];
        switch(v.getId()) {
            case R.id.btnArtists:
                //retrieve artists from database
                temp = db.getArtists();
                currentDisplay = display.ARTISTS;
                break;
            case R.id.btnAlbums:
                //retrieve albums from database
                temp = db.getAlbums();
                currentDisplay = display.ALBUMS;
                break;
            case R.id.btnPlaylists:
                //retrieve playlists from database
                temp = db.getPlaylists();
                currentDisplay = display.PLAYLISTS;
                break;
            case R.id.btnGenre:
                //retrieve genres from database
                // TODO: 11/15/2015 uncomment this when database is working
//                tempList = db.getGenres();
//                currentDisplay = display.GENRES;
                break;
            case R.id.btnSongs:
                //retrieve songs from database
                temp = db.getSongTitles();
                currentListMediaFiles.clear();
                currentListMediaFiles.addAll(Arrays.asList(db.getMediaFiles()));
                currentDisplay = display.SONGS;
                break;

        }

        // update displayed list via adapter
        currentList.clear();
        currentList.addAll(Arrays.asList(temp));
        adapter.notifyDataSetChanged();
    }

    //onClick listener for clickable display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //save reference to the item that was clicked as the string that was displayed
        String clicked = currentList.get(position);
        // set list to artists
        if (currentDisplay == display.ARTISTS) {

            // add all artists from database
            currentList.clear();
            currentList.addAll(Arrays.asList(db.getAlbums(clicked)));
            // update current view
            currentDisplay = display.ALBUMS;
            //update display
            adapter.notifyDataSetChanged();
        }
        else if (currentDisplay == display.ALBUMS) {
            // media to display
            ArrayList<SongData> songs = new ArrayList<>();
            songs.addAll(Arrays.asList(db.getSongs(clicked)));

            // get all the songs in that album
            currentListMediaFiles.addAll(Arrays.asList(db.getMediaFilesFromAlbum(clicked)));

            // add to current list
            currentList.clear();
            for(SongData song : songs) {
                currentList.add(song.getTitle());
            }

            // update current view
            currentDisplay = display.SONGS;
            //update display
            adapter.notifyDataSetChanged();
        }
        else if(currentDisplay == display.SONGS) {

            // get sublist that contains media files beyond the position clicked
            MediaFile[] temp = new MediaFile[currentListMediaFiles.size() - position];
            for(int i = 0; i < temp.length; i ++) {
                temp[i] = currentListMediaFiles.get(i + position);
            }

            returnIntent(temp);
        }
        else if (currentDisplay == display.PLAYLISTS) {
            mediaFiles = db.getMediaFilesFromPlaylist(clicked);
            // return to main activity
            returnIntent(mediaFiles);
        }
        else {
            // TODO: 11/15/2015 uncomment this when database is working
//            mediaFiles = db.getSongsData(position);
        }

        // update current list
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // TODO: 11/29/2015 instead of setItems, use setAdapter(ListAdapter) to set a dynamic list of all playlists, or option to add new playlist

        // build a new dialog box for choosing which playlist to add the item(s) to
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dialog Message")
                .setTitle("Select Playlist")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ResultListsActivity.this, "Add was clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("New", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ResultListsActivity.this, "Adding new Playlist", Toast.LENGTH_SHORT).show();
                        // TODO: 11/29/2015 make this prompt for playlist title, and add playlist
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ResultListsActivity.this, "Cancel was clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setItems(R.array.menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ResultListsActivity.this, "Item " + which + " was clicked", Toast.LENGTH_SHORT).show();
                    }
                });

        // build dialog
        AlertDialog dialog = builder.create();

        // show dialog
        dialog.show();

        // return we handled the event
        return true;
    }

    private void returnIntent(MediaFile[] mediaFiles) {
        // bundle the MediaFiles and return them to MainActivity as intent
        Intent intent = getIntent();
        Bundle returnedFiles = new Bundle();
        returnedFiles.putSerializable(RESULT_LIST_ACTIVITY_RESPONSE_KEY, mediaFiles);
        intent.putExtra(RESULT_LIST_ACTIVITY_RESPONSE_KEY, returnedFiles);
        // set the result
        setResult(Activity.RESULT_OK, intent);
        // finish activity
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("currentList", currentList);
    }
}
