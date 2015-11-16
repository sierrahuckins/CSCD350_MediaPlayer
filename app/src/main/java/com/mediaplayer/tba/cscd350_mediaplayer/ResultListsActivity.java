package com.mediaplayer.tba.cscd350_mediaplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Sierra on 11/8/2015.
 *
 */
public class ResultListsActivity extends AppCompatActivity implements View.OnClickListener {

    Button artistsBtn;
    Button albumsBtn;
    Button playlistsBtn;
    Button songsBtn;

    ListView listResults;

    ArrayList<String> currentList; // has to be an arraylist because the adapter needs a dynamic list
    ArrayAdapter<String> adapter;
    LibraryDatabase db = new LibraryDatabase(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_resultslistsactivity);

        currentList = new ArrayList<>();
        currentList.add("Choose A Category");

        artistsBtn = (Button)findViewById(R.id.btnArtists);
        albumsBtn = (Button)findViewById(R.id.btnAlbums);
        playlistsBtn = (Button)findViewById(R.id.btnPlaylists);
        songsBtn = (Button)findViewById(R.id.btnSongs);
        listResults = (ListView)findViewById(R.id.listResults);

        artistsBtn.setOnClickListener(this);
        albumsBtn.setOnClickListener(this);
        playlistsBtn.setOnClickListener(this);
        songsBtn.setOnClickListener(this);

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            String[] temp = savedInstanceState.getStringArray("currentList");
            if(temp != null) {
                currentList = new ArrayList<>();
                currentList.addAll(Arrays.asList(temp));
            }
        }

        // get new adapter and pass it a list item layout and the text view in the list item
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, currentList);
        // set the adapter on the list in content_resultslistactivity
        listResults.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        String[] resultsList = null;

        switch(v.getId()) {
            case R.id.btnArtists:
                //retrieve artists from database
                resultsList = db.getArtists();
                break;
            case R.id.btnAlbums:
                //retrieve albums from database
                resultsList = db.getAlbums();
                break;
            case R.id.btnPlaylists:
                //retrieve playlists from database
                //resultsList = db.getPlaylists();
                break;
            case R.id.btnSongs:
                //retrieve songs from database
//                resultsList = db.getSongs();
                break;
        }

        if(resultsList != null) {
            currentList = new ArrayList<>();
            currentList.addAll(Arrays.asList(resultsList));
        }

        adapter.clear();
        adapter.addAll(currentList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArray("currentList",currentList.toArray(new String[currentList.size()]));
    }
}
