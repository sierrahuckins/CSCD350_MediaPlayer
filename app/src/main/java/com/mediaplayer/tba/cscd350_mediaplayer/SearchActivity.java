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

    // search field
    EditText mSearchField;

    //holds the current list of strings that is being displayed
    private ArrayList<String> currentListStrings;
    private ArrayList<MediaFile> currentListMediaFiles;

    private ArrayAdapter<String> adapter;
    private LibraryDatabase db = new LibraryDatabase(this);

    // activity intent request response key
    public static final String SEARCH_ACTIVITY_RESPONSE_KEY = "response_key";

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //initialize references to all buttons
        Button searchBtn = (Button)findViewById(R.id.btnSearch);
        mSearchField = (EditText)findViewById(R.id.txtfldSearch);;
        ListView listResults = (ListView)findViewById(R.id.listResults);

        //set on click listeners for buttons and clickable display
        searchBtn.setOnClickListener(this);
        listResults.setOnItemClickListener(this);

        // create current list
        currentListStrings = new ArrayList<>();
        currentListMediaFiles = new ArrayList<>();

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            ArrayList<String> temp = (ArrayList<String>) savedInstanceState.getSerializable("currentListStrings");
            if(temp != null) {
                currentListStrings.addAll(temp);
            }
        }

        // get new adapter and pass it a list item layout and the text view in the list item
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, currentListStrings);
        // set the adapter on the list in content_resultslistactivity
        listResults.setAdapter(adapter);
    }

    //onClick listener for buttons
    @Override
    public void onClick(View v) {
        String searchValue = String.valueOf(mSearchField.getText().toString());

        if (searchValue.equals("")) {
            Toast.makeText(this, "Please enter a search request above!", Toast.LENGTH_LONG).show();
        }
        else {
            //retrieve results from database
            MediaFile[] mediaFiles = db.search(searchValue);

            // add to list of media files
            currentListMediaFiles.clear();
            currentListMediaFiles.addAll(Arrays.asList(mediaFiles));

            // make array of string titles of media files
            String[] tempList = new String[mediaFiles.length];
            for (int x = 0; x < mediaFiles.length; x++) {
                tempList[x] = mediaFiles[x].toString();
            }

            // update displayed list via adapter
            currentListStrings.clear();
            currentListStrings.addAll(Arrays.asList(tempList));
            adapter.notifyDataSetChanged();
        }
    }

    //onClick listener for clickable display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get single item from click
        MediaFile temp = currentListMediaFiles.get(position);

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

        outState.putSerializable("currentListStrings", currentListStrings);
    }
}
