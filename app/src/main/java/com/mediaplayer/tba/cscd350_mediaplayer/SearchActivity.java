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
public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //gui references
    private Button searchBtn;
    private EditText searchField;
    private ListView listResults;

    //intent request response key
    public static final String SEARCH_ACTIVITY_RESPONSE_KEY = "response_key";

    //holds the current list of strings that is being displayed
    //and the media files associated with those strings
    private ArrayList<String> currentListStrings = new ArrayList<>();
    private ArrayList<MediaFile> currentListMediaFiles = new ArrayList<>();

    //adapter for display
    private ArrayAdapter<String> adapter;

    //reference to app's internal database
    private LibraryDatabase db = new LibraryDatabase(this);

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

        // get new adapter and pass it a list item layout and the text view in the list item
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, currentListStrings);

        // set the adapter on the list in content_resultslistactivity
        listResults.setAdapter(adapter);
    }

    private void updateSavedInstanceState(Bundle savedInstanceState) {
        //update saved strings list data and mediaFiles list data
        currentListStrings.addAll((ArrayList<String>) savedInstanceState.getSerializable("currentListStrings"));
        currentListMediaFiles.addAll((ArrayList<MediaFile>) savedInstanceState.getSerializable("currentListMediaFiles"));
    }

    private void initializeOnClickListeners() {
        searchBtn.setOnClickListener(this);
        listResults.setOnItemClickListener(this);
    }

    private void initializeGUIReferences() {
        searchBtn = (Button)findViewById(R.id.btnSearch);
        searchField = (EditText)findViewById(R.id.txtfldSearch);
        listResults = (ListView)findViewById(R.id.listResults);
    }

    //onClick listener for button
    @Override
    public void onClick(View v) {
        //get value to be used as search parameter from text field
        String searchValue = String.valueOf(searchField.getText().toString());

        //show toast if user tried to search for nothing
        if (searchValue.equals("")) {
            Toast.makeText(this, "Please enter a search request above!", Toast.LENGTH_LONG).show();
        }
        //else show results from database
        else {
            //retrieve results from database
            MediaFile[] mediaFiles = db.search(searchValue);

            //update media files and display
            updateMediaFiles(mediaFiles);
            updateDisplay(mediaFiles);
        }
    }

    private void updateMediaFiles(MediaFile[] mediaFiles) {
        //add to list of media files
        currentListMediaFiles.clear();
        currentListMediaFiles.addAll(Arrays.asList(mediaFiles));
    }

    private void updateDisplay(MediaFile[] mediaFiles) {
        // update displayed list via adapter
        currentListStrings.clear();

        //copy new list to display list
        for (int x = 0; x < mediaFiles.length; x++) {
            currentListStrings.add(mediaFiles[x].toString());
        }

        //notify adapter so display gets updated
        adapter.notifyDataSetChanged();
    }

    //onClick listener for clickable display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get sublist that contains media files beyond the position clicked
        MediaFile[] temp = new MediaFile[currentListMediaFiles.size() - position];

        for(int i = 0; i < temp.length; i ++) {
            temp[i] = currentListMediaFiles.get(i + position);
        }

        // return to calling activity
        returnIntent(temp);
    }

    private void returnIntent(MediaFile[] mediaFiles) {
        // bundle the MediaFiles
        Bundle returnedFiles = new Bundle();
        returnedFiles.putSerializable(SEARCH_ACTIVITY_RESPONSE_KEY, mediaFiles);

        //create intent to return and put bundle into  it
        Intent intent = getIntent();
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
        outState.putSerializable("currentListStrings", currentListStrings);

        //save current media files that go with display for later reinitialization
        outState.putSerializable("currentMediaFiles", currentListMediaFiles);
    }
}
