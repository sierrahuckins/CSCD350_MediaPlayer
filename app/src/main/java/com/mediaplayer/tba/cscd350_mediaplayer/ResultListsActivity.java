package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


/**
*ResultsListsActivity.java
*Author: Sierra Huckins
*Revision: 1
*Date: 20151113
*Rev. Author: Sierra Huckins
*Description: Activity that allows user to display database contents based on 1 of 5 factors: artists, albums, playlists, genres, and songs.
*Results from user's choice within the displayed contents are returned to the MainActivity for playing.
**/
public class ResultListsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //member variables for buttons and clickable display
    private Button artistsBtn;
    private Button albumsBtn;
    private Button playlistsBtn;
    private Button genreBtn;
    private Button songsBtn;
    private ListView listResults;

    //holds the array list of MediaFiles that will be returned to MainActivity
    private ArrayList<MediaFile> mediaFiles;

    //holds the current list of strings that is being displayed
    private ArrayList<String> currentList;
    private enum display {ARTISTS, ALBUMS, PLAYLISTS, SONGS, GENRES}
    private display currentDisplay;

    private ArrayAdapter<String> adapter;
    private LibraryDatabase db = new LibraryDatabase(this);

    // activity intent request response key
    public static final String RESULT_LIST_ACTIVITY_RESPONSE_KEY = "response_key";

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_resultslistsactivity);

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

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            ArrayList<String> temp = (ArrayList<String>) savedInstanceState.getSerializable("currentList");
            if(temp != null) {
                currentList.addAll(temp);
            }
        }
        else {
            currentList.addAll(Arrays.asList(db.getArtists()));
        }

        // get new adapter and pass it a list item layout and the text view in the list item
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, currentList);
        // set the adapter on the list in content_resultslistactivity
        listResults.setAdapter(adapter);
    }

    //onClick listener for buttons
    @Override
    public void onClick(View v) {

        String[] tempList = new String[0];
        switch(v.getId()) {
            case R.id.btnArtists:
                //retrieve artists from database
                tempList = db.getArtists();
                break;
            case R.id.btnAlbums:
                //retrieve albums from database
                tempList = db.getAlbums();
                break;
            case R.id.btnPlaylists:
                //retrieve playlists from database
                tempList = db.getPlaylists();
                break;
            case R.id.btnGenre:
                //retrieve genres from database
                // TODO: 11/15/2015 uncomment this when database is workin
//                tempList = db.getGenres();
            case R.id.btnSongs:
                //retrieve songs from database
                // TODO: 11/15/2015 uncomment this when database is workin
                tempList = db.getSongTitles();
                break;

        }

        // update displayed list via adapter
        currentList.clear();
        currentList.addAll(Arrays.asList(tempList));
        adapter.notifyDataSetChanged();
    }

    //onClick listener for clickable display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //save reference to the item that was clicked as the string that was displayed
        String clicked = (String)parent.getItemAtPosition(position);

        //if currentDisplay was of artists, then we want display albums now
        //rather than go straight to playing whole artist
        if (currentDisplay == display.ARTISTS) {
            currentList.clear();
            currentList.addAll(Arrays.asList(db.getAlbums()));
            currentDisplay = display.ALBUMS;

            //update display
            adapter.clear();
            adapter.addAll(currentList);
            adapter.notifyDataSetChanged();
        }
        //or every other display we will get a list of MediaFiles from database
        //to be returned to MainActivity
        else if (currentDisplay == display.ALBUMS) {
            // TODO: 11/15/2015 uncomment this when database is working
//            mediaFiles = db.getAlbumData(clicked);
        }
        else if (currentDisplay == display.PLAYLISTS) {
            // TODO: 11/15/2015 uncomment this when database is working
//            mediaFiles = db.getPlaylistData(clicked);
        }
        else {
            // TODO: 11/15/2015 uncomment this when database is working
//            mediaFiles = db.getSongsData(position);
        }

        // bundle the MediaFiles and return them to MainActivity as intent
        Intent intent = getIntent();
        Bundle returnedFiles = new Bundle();
        returnedFiles.putSerializable(RESULT_LIST_ACTIVITY_RESPONSE_KEY, mediaFiles);
        intent.putExtra(RESULT_LIST_ACTIVITY_RESPONSE_KEY, returnedFiles);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("currentList", currentList);
    }
}
