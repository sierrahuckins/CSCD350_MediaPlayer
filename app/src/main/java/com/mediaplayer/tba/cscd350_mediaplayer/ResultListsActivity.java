package com.mediaplayer.tba.cscd350_mediaplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.ResultSet;
import java.util.List;


/**
 * Created by Sierra on 11/8/2015.
 */
public class ResultListsActivity extends AppCompatActivity implements View.OnClickListener {
    Button artistsBtn = (Button)findViewById(R.id.btnArtists);
    Button albumsBtn = (Button)findViewById(R.id.btnAlbums);
    Button playlistsBtn = (Button)findViewById(R.id.btnPlaylists);
    Button songsBtn = (Button)findViewById(R.id.btnSongs);
    ListView listResults = (ListView)findViewById(R.id.listResults);
    String[] currentList = {"Choose A Category"};
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_resultslistsactivity);

        artistsBtn.setOnClickListener(this);
        albumsBtn.setOnClickListener(this);
        playlistsBtn.setOnClickListener(this);
        songsBtn.setOnClickListener(this);

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            currentList = savedInstanceState.getStringArray("currentList");
        }

        adapter = new ArrayAdapter<>(this,R.layout.content_resultslistsactivity,currentList);
        listResults.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        List<String> resultsList;

        switch(v.getId()) {
            case R.id.btnArtists:
                //retrieve artists from database
                resultsList =
                break;
            case R.id.btnAlbums:
                //retrieve albums from database
                resultsList =
                break;
            case R.id.btnPlaylists:
                //retrieve playlists from database
                resultsList =
                break;
            case R.id.btnSongs:
                //retrieve songs from database
                resultsList =
                break;
        }

        currentList = new String[resultsList.size()];
        int i = 0;

        for(String s: resultsList) {
            currentList[i] = s;
            i++;
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArray("currentList",currentList);
    }
}
