package com.mediaplayer.tba.cscd350_mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // navigation drawer
    DrawerLayout drawerLayout;
    DrawerAdapter drawerAdapter;
    ListView drawerLayoutListView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    // media player
    MediaPlayer mediaPlayer;

    // buttons
    Button playPauseButton;
    boolean playPauseButtonState = false;
    String playPauseButtonTextPlaying = "Play";
    String playPauseButtonTextPaused = "Pause";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: 11/5/2015 put this in a method that Sierra can call from her menu, or by passing an intent
        // media player and attach it to a file in the resource directory
        mediaPlayer = MediaPlayer.create(this, R.raw.bits);
        // start media playback
        mediaPlayer.start();

        // get the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // get the drawer list view
        drawerLayoutListView = (ListView) findViewById(R.id.left_drawer);

        // add drawer item to drawer item list view
        // create array
        DrawerItem[] drawerItems = new DrawerItem[1];
        // populate array
        drawerItems[0] = new DrawerItem(R.drawable.check_box_icon, "Check");

        // create drawer adapter with the list of drawer items
        drawerAdapter = new DrawerAdapter(this, R.layout.drawer_item, drawerItems);
        // set the drawer adapter on the drawer
        drawerLayoutListView.setAdapter(drawerAdapter);

        // get the play pause button from the activity
        playPauseButton = (Button) findViewById(R.id.playPauseButton);

        // create item click listener
        ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
            // onClick for drawerLayout listView items
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // change all the drawer menu item text views
                TextView textView = (TextView) findViewById(R.id.drawer_item_text_view);
                textView.setText("Click");

                // close the drawer layout once an item is clicked
                drawerLayout.closeDrawers();
            }
        };
        // set this as an onClick listener for the drawer items
        drawerLayoutListView.setOnItemClickListener(onItemClickListener);

        // button onClickListener
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the button from the view
                Button button = (Button) findViewById(R.id.playPauseButton);
                if(mediaPlayer != null) {
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    else {
                        mediaPlayer.start();
                    }
                    // change the text on the button
                    button.setText(mediaPlayer.isPlaying() ? playPauseButtonTextPaused : playPauseButtonTextPlaying);
                }

            }
        };
        // set onClickListener on button
        playPauseButton.setOnClickListener(onClickListener);

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

    // play all media files in an array of media files
    /*
     * this should set a listener on the mediaPlayer, and whenever one of the mediaFiles ends, it should start the next one.
     * if we are set on repeat, we should just replay the current media file
     * if we are set to repeat all, we should replay the list of mediaFiles
     */
    public void playMediaFiles(MediaFile[] mediaFiles) {



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
