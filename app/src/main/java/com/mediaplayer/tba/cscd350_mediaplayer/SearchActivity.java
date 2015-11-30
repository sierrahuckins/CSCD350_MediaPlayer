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

    //member variables for buttons and clickable display
    private Button searchBtn;
    private ListView listResults;
    private EditText searchTxtFld;

    //holds the array list of MediaFiles that will be returned to MainActivity
    private MediaFile[] mediaFiles;

    //holds the current list of strings that is being displayed
    private ArrayList<String> currentList;
    private ArrayList<SongData> currentListSongData;

    private ArrayAdapter<String> adapter;
    private LibraryDatabase db = new LibraryDatabase(this);

    // activity intent request response key
    public static final String RESULT_LIST_ACTIVITY_RESPONSE_KEY = "response_key";

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        //initialize references to all buttons
        searchBtn = (Button)findViewById(R.id.btnSearch);
        searchTxtFld = (EditText)findViewById(R.id.txtfldSearch);
        listResults = (ListView)findViewById(R.id.listResults);

        //set on click listeners for buttons and clickable display
        searchBtn.setOnClickListener(this);
        listResults.setOnItemClickListener(this);

        // create current list
        currentList = new ArrayList<>();
        currentListSongData = new ArrayList<>();

        //update list if there is saved instance data from before
        if (savedInstanceState != null) {
            ArrayList<String> temp = (ArrayList<String>) savedInstanceState.getSerializable("currentList");
            // TODO: 11/22/2015 get currentDisplay as well
            if(temp != null) {
                currentList.addAll(temp);
            }
        }

        // get new adapter and pass it a list item layout and the text view in the list item
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_entry, currentList);
        // set the adapter on the list in content_resultslistactivity
        listResults.setAdapter(adapter);
    }

    //onClick listener for buttons
    @Override
    public void onClick(View v) {
        String searchValue = String.valueOf(searchTxtFld.getText().toString());

        if (searchValue.equals("")) {
            Toast.makeText(this, "Please enter a search request above!", Toast.LENGTH_LONG).show();
        }
        else {
            //retrieve results from database
            mediaFiles = db.search(searchValue);

            String[] tempList = new String[mediaFiles.length];

            for (int x = 0; x < mediaFiles.length; x++) {
                tempList[x] = mediaFiles[x].toString();
            }

            // update displayed list via adapter
            currentList.clear();
            currentList.addAll(Arrays.asList(tempList));
            adapter.notifyDataSetChanged();
        }
    }

    //onClick listener for clickable display
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO return clicked item
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
