package com.mediaplayer.tba.cscd350_mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
*SearchActivity.java
*Author: Sierra Huckins
*Revision: 1
*Date: 20151130
*Rev. Author: Sierra Huckins
*Description: Activity that allows user to search database for given string within title, album, artist, and genre fields.
*Results are returned as a list on screen. User can then choose result from that list to be returned to main screen for playing.
**/
public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    //gui references
    private Button mSearchButton;
    private EditText mSearchField;
    private ListView mListResults;

    //intent request response key
    public static final String SEARCH_ACTIVITY_RESPONSE_KEY = "response_key";

    //holds the current list of strings that is being displayed and the media files associated with those strings
    private ArrayList<String> mCurrentListStrings = new ArrayList<>();
    private ArrayList<MediaFile> mCurrentListMediaFiles = new ArrayList<>();

    //mAdapter for display
    private ArrayAdapter<String> mAdapter;

    //reference to app's internal database
    private LibraryDatabase mDB = new LibraryDatabase(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //initialize references to gui elements
        initializeGUIReferences();

        //set on click listeners for buttons and clickable display
        initializeOnClickListeners();

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            updateSavedInstanceState(savedInstanceState);
        }

        // get new mAdapter and pass it a list item layout and the text view in the list item
        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, mCurrentListStrings);

        // set the mAdapter on the list in content_resultslistactivity
        mListResults.setAdapter(mAdapter);
    }

    private void updateSavedInstanceState(Bundle savedInstanceState) {
        //update saved strings list data and mediaFiles list data
        mCurrentListStrings.addAll((ArrayList<String>) savedInstanceState.getSerializable("mCurrentListStrings"));
        mCurrentListMediaFiles.addAll((ArrayList<MediaFile>) savedInstanceState.getSerializable("mCurrentListMediaFiles"));
    }

    private void initializeOnClickListeners() {
        mSearchButton.setOnClickListener(this);
        mListResults.setOnItemClickListener(this);
    }

    private void initializeGUIReferences() {
        mSearchButton = (Button)findViewById(R.id.search_button);
        mSearchField = (EditText)findViewById(R.id.search_text_field);
        mListResults = (ListView)findViewById(R.id.search_list);
    }

    //onClick listener for button
    @Override
    public void onClick(View v) {
        String searchValue = String.valueOf(mSearchField.getText().toString());

        //show toast if user tried to search for nothing
        if (searchValue.equals("")) {
            Toast.makeText(this, "Please enter a search request above!", Toast.LENGTH_LONG).show();
        }
        //else show results from database
        else {
            //retrieve results from database
            MediaFile[] mediaFiles = mDB.search(searchValue);

            //update media files and display
            updateMediaFiles(mediaFiles);
            updateDisplay(mediaFiles);
        }
    }

    private void updateMediaFiles(MediaFile[] mediaFiles) {
        //add to list of media files
        mCurrentListMediaFiles.clear();
        mCurrentListMediaFiles.addAll(Arrays.asList(mediaFiles));
    }

    private void updateDisplay(MediaFile[] mediaFiles) {
        // update displayed list via mAdapter
        mCurrentListStrings.clear();

        //copy new list to display list
        for (MediaFile mediaFile : mediaFiles) {
            mCurrentListStrings.add(mediaFile.toString());
        }

        //notify mAdapter so display gets updated
        mAdapter.notifyDataSetChanged();
    }

    //onClick listener for clickable display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get single item from click
        MediaFile temp = mCurrentListMediaFiles.get(position);

        // return to calling activity
        returnIntent(temp);
    }

    private void returnIntent(MediaFile mediaFile) {
        // bundle the MediaFiles and return them to MainActivity as intent
        Intent intent = getIntent();
        Bundle returnedFiles = new Bundle();
        returnedFiles.putSerializable(SEARCH_ACTIVITY_RESPONSE_KEY, mediaFile);
        intent.putExtra(SEARCH_ACTIVITY_RESPONSE_KEY, returnedFiles);
        // set the result
        setResult(Activity.RESULT_OK, intent);
        // finish activity
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save what is currently being displayed so it can be reinitialized after state change
        outState.putSerializable("mCurrentListStrings", mCurrentListStrings);

        //save current media files that go with display for later reinitialization
        outState.putSerializable("currentMediaFiles", mCurrentListMediaFiles);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // check which view was long clicked
        switch (view.getId()) {
            case R.id.search_list:
                // get the media file that represents the position clicked
                MediaFile mediaFile = mCurrentListMediaFiles.get(position);
                // show the dialog to add to playlist
                MusicPlayerDialog.selectPlaylistDialog(SearchActivity.this, mDB, mediaFile);
        }
        // return event handled true
        return true;
    }
}
