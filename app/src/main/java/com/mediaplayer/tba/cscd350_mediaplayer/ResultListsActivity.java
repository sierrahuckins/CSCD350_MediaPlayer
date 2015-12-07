package com.mediaplayer.tba.cscd350_mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * ResultsListsActivity.java
 * Author: Sierra Huckins
 * Revision: 1
 * Date: 20151113
 * Rev. Author: Sierra Huckins
 * Description: Activity that allows user to Display database contents based on 1 of 5 factors: artists, albums, playlists, genres, and songs.
 * Results from user's choice within the displayed contents are returned to the MainActivity for playing.
 * Revision: 2
 * Date: 20151130
 * Rev. Author: Bruce Emehiser
 * Description: Shows dialog box on long click for manipulation of playlists
 **/
public class ResultListsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //gui references
    private Button artistsBtn;
    private Button albumsBtn;
    private Button playlistsBtn;
    private Button genreBtn;
    private Button songsBtn;
    private ListView listResults;

    //intent request response keys
    public static final String CURRENT_LIST_KEY = "mCurrentList";
    public static final String CURRENT_DISPLAY_KEY = "mCurrentDisplayState";

    //holds the current list of strings that is being displayed
    private ArrayList<String> mCurrentList = new ArrayList<>();
    private ArrayList<MediaFile> mCurrentListMediaFiles = new ArrayList<>();

    //enum class representing current Display states
    private enum Display {ARTISTS, ALBUMS, PLAYLISTS, SONGS, GENRES}
    private Display mCurrentDisplayState;

    private ArrayAdapter<String> mAdapter;
    private LibraryDatabase mDB = new LibraryDatabase(this);

    // activity intent request response key
    public static final String RESULT_LIST_ACTIVITY_RESPONSE_KEY = "response_key";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        //initialize references to all buttons
        initializeGUIReferences();

        //set on click listeners for buttons and clickable Display
        initializeOnClickListeners();

        //update list if there is saved instance data from before
        updateSavedInstanceState(savedInstanceState);

        // get new mAdapter and pass it a list item layout and the text view in the list item
        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, mCurrentList);
        // set the mAdapter on the list in content_resultslistactivity
        listResults.setAdapter(mAdapter);

        // register as a long click listener. we will show a dialog box
        listResults.setOnItemLongClickListener(this);
    }

    private void updateSavedInstanceState(Bundle savedInstanceState) {
        //sets saved instance state data if there was a previously saved instance state
        if (savedInstanceState != null) {
            ArrayList<String> temp = (ArrayList<String>) savedInstanceState.getSerializable(CURRENT_LIST_KEY);
            Display display = (Display) savedInstanceState.getSerializable(CURRENT_DISPLAY_KEY);
            if(temp != null) {
                mCurrentList.addAll(temp);
                mCurrentDisplayState = display;
            }
        }
        //otherwise sets default data
        else {
            mCurrentList.addAll(Arrays.asList(mDB.getArtists()));
            mCurrentDisplayState = Display.ARTISTS;
        }
    }

    private void initializeOnClickListeners() {
        artistsBtn.setOnClickListener(this);
        albumsBtn.setOnClickListener(this);
        playlistsBtn.setOnClickListener(this);
        genreBtn.setOnClickListener(this);
        songsBtn.setOnClickListener(this);
        listResults.setOnItemClickListener(this);
    }

    private void initializeGUIReferences() {
        artistsBtn = (Button) findViewById(R.id.btnArtists);
        albumsBtn = (Button) findViewById(R.id.btnAlbums);
        playlistsBtn = (Button) findViewById(R.id.btnPlaylists);
        genreBtn = (Button) findViewById(R.id.btnGenre);
        songsBtn = (Button) findViewById(R.id.btnSongs);
        listResults = (ListView) findViewById(R.id.listResults);
    }

    @Override
    public void onClick(View v) {
        String[] temp = new String[0];
        switch(v.getId()) {
            case R.id.btnArtists:
                //retrieve artists from database
                temp = mDB.getArtists();
                mCurrentDisplayState = Display.ARTISTS;
                break;
            case R.id.btnAlbums:
                //retrieve albums from database
                temp = mDB.getAlbums();
                mCurrentDisplayState = Display.ALBUMS;
                break;
            case R.id.btnPlaylists:
                //retrieve playlists from database
                temp = mDB.getPlaylists();
                mCurrentDisplayState = Display.PLAYLISTS;
                break;
            case R.id.btnGenre:
                //retrieve genres from database
                temp = mDB.getGenres();
                mCurrentDisplayState = Display.GENRES;
                break;
            case R.id.btnSongs:
                //retrieve songs from database
                temp = mDB.getSongTitles();
                // update current list of media files
                mCurrentListMediaFiles.clear();
                mCurrentListMediaFiles.addAll(Arrays.asList(mDB.getMediaFiles()));
                mCurrentDisplayState = Display.SONGS;
                break;

        }

        // update displayed list via mAdapter
        mCurrentList.clear();
        mCurrentList.addAll(Arrays.asList(temp));
        mAdapter.notifyDataSetChanged();
    }

    //onClick listener for clickable Display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //save reference to the item that was clicked as the string that was displayed
        String clicked = mCurrentList.get(position);
        // set list to artists
        if (mCurrentDisplayState == Display.ARTISTS) {

            // add all artists from database
            mCurrentList.clear();
            mCurrentList.addAll(Arrays.asList(mDB.getAlbums(clicked)));
            // update current view
            mCurrentDisplayState = Display.ALBUMS;
            //update Display
            mAdapter.notifyDataSetChanged();
        }
        else if (mCurrentDisplayState == Display.ALBUMS) {
            // media to Display
            ArrayList<SongData> songs = new ArrayList<>();
            songs.addAll(Arrays.asList(mDB.getSongs(clicked)));

            // get all the songs in that album
            mCurrentListMediaFiles.addAll(Arrays.asList(mDB.getMediaFilesFromAlbum(clicked)));

            // add to current list
            mCurrentList.clear();
            for(SongData song : songs) {
                mCurrentList.add(song.getTitle());
            }

            // update current view
            mCurrentDisplayState = Display.SONGS;
            //update Display
            mAdapter.notifyDataSetChanged();
        }
        else if(mCurrentDisplayState == Display.SONGS) {

            // get sublist that contains media files beyond the position clicked
            MediaFile[] temp = new MediaFile[mCurrentListMediaFiles.size() - position];
            for(int i = 0; i < temp.length; i ++) {
                temp[i] = mCurrentListMediaFiles.get(i + position);
            }

            returnIntent(temp);
        }
        else if (mCurrentDisplayState == Display.PLAYLISTS) {
            MediaFile[] temp = mDB.getMediaFilesFromPlaylist(clicked);
            // return to main activity
            returnIntent(temp);
        }
        else if(mCurrentDisplayState == Display.GENRES) {

            // get all the songs in that album
            mCurrentListMediaFiles.clear();
            mCurrentListMediaFiles.addAll(Arrays.asList(mDB.getMediaFilesFromGenre(clicked)));

            // add to current list
            mCurrentList.clear();
            for(MediaFile m : mCurrentListMediaFiles) {
                mCurrentList.add(m.getTitle());
            }

            // update current view
            mCurrentDisplayState = Display.SONGS;
            //update Display
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        switch (mCurrentDisplayState) {

            case ARTISTS:
                MusicPlayerDialog.selectPlaylistDialog(ResultListsActivity.this, mDB, mDB.getMediaFilesFromArtist(mCurrentList.get(position)));
                break;
            case ALBUMS:
                MusicPlayerDialog.selectPlaylistDialog(ResultListsActivity.this, mDB, mDB.getMediaFilesFromAlbum(mCurrentList.get(position)));
                break;
            case SONGS:
                MediaFile[] temp = new MediaFile[1];
                temp[0] = mCurrentListMediaFiles.get(position);
                MusicPlayerDialog.selectPlaylistDialog(ResultListsActivity.this, mDB, temp);
                break;
            case GENRES:
                Toast.makeText(ResultListsActivity.this, "Genres not yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case PLAYLISTS:

                String playlist = mCurrentList.get(position);
                // prompt for user confirmation
                MusicPlayerDialog.removePlaylistDialog(ResultListsActivity.this, mDB, playlist);
                // remove from list view
                mCurrentList.remove(playlist);
                // notiy mAdapter of change
                mAdapter.notifyDataSetChanged();
                break;
        }

        // return we handled the event
        return true;
    }

    private void returnIntent(MediaFile[] mediaFiles) {
        // guard against android.os.TransactionTooLargeException by only returning subset
        if(mediaFiles.length > 999) {
            mediaFiles = Arrays.copyOfRange(mediaFiles, 0, 999);
        }
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

        outState.putSerializable(CURRENT_LIST_KEY, mCurrentList);
        outState.putSerializable(CURRENT_DISPLAY_KEY, mCurrentDisplayState);
    }
}
