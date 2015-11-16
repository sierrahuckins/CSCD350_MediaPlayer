package com.mediaplayer.tba.cscd350_mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // navigation drawer
    DrawerLayout drawerLayout;
    DrawerAdapter drawerAdapter;
    ListView drawerLayoutListView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LibraryDatabase database;

    // media player
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    ArrayList<MediaFile> nowPlaying;

    public static final int REQUEST_CODE_RESULT_LIST_ACTIVITY = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create a new instance of the database
        if(database == null)
            database = new LibraryDatabase(this);

        // call the file search to search for files in the system
        Runnable fileSearchAndAddThread = new Runnable() {
            @Override
            public void run() {
                FilesSearch filesSearch = new FilesSearch();
                MediaFile[] mediaFiles = filesSearch.scanFileSystem(getApplicationContext());

                // add all the mediafiles we just found to our database
                // TODO: 11/9/2015 make this run on first launch, on a storage isChanged listener on the ContentResolver, or when the users says to
                database.populateDatabase(mediaFiles); // if this runs every launch, it will produce a zillion errors because of database collision
            }
        };
        // run the thread
        fileSearchAndAddThread.run();


        // get the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // get the drawer list view
        drawerLayoutListView = (ListView) findViewById(R.id.left_drawer);

        // add drawer item to drawer item list view
        // create array
        DrawerItem[] drawerItems = new DrawerItem[2];
        // populate array
        drawerItems[0] = new DrawerItem(R.drawable.headphones, "Library");
        drawerItems[1] = new DrawerItem(android.R.drawable.ic_search_category_default, "Search");

                // create drawer adapter with the list of drawer items
        drawerAdapter = new DrawerAdapter(this, R.layout.drawer_item, drawerItems);
        // set the drawer adapter on the drawer
        drawerLayoutListView.setAdapter(drawerAdapter);

        // create item click listener
        ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
            // onClick for drawerLayout listView items
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent intent = new Intent(view.getContext(), ResultListsActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_RESULT_LIST_ACTIVITY);
                        break;
                }

                // close the drawer layout once an item is clicked
                drawerLayout.closeDrawers();
            }
        };
        // set this as an onClick listener for the drawer items
        drawerLayoutListView.setOnItemClickListener(onItemClickListener);

        // this is the animaged toggle button which opens and closes the drawer
        // set action bar drawer toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(getString(R.string.drawer_closed));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(getString(R.string.drawer_open));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // set drawer layout as a listener on action bar drawer toggle
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        ActionBar actionBar = getSupportActionBar();
        // set back button state
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        // sync state
        actionBarDrawerToggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == REQUEST_CODE_RESULT_LIST_ACTIVITY && resultCode == Activity.RESULT_OK) {

            // TODO: 11/15/2015 process the request result
        }

    }

    private void play() {

        // check for null
        if(mediaPlayer == null) {
            // play next file in playlist
            playNext();
        }
        // see if it is paused
        else if(! mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void pause() {
        // check for null
        if(mediaPlayer == null) {
            return;
        }
        // if media player is playing, pause it
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void playNext() {
        // check for null player
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        // check for no next items
        if(nowPlaying.size() == 0) {
            // no items to play
            return;
        }
        // get next media file and remove it from the now playing list
        MediaFile mediaFile = nowPlaying.get(0);
        nowPlaying.remove(0);

        // prepare the media player and start it
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this, mediaFile.getUri());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("IOException", "Cannot play media file," + mediaFile.toString() + " Invalid Uri (or something like that)");
            return;
        }
        mediaPlayer.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerLayoutListView);
        // if the drawer is open, hide the button settings
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
