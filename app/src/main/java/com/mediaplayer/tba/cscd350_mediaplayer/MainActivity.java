package com.mediaplayer.tba.cscd350_mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // navigation drawer
    DrawerLayout mDrawerLayout;
    DrawerAdapter mDrawerAdapter;
    ListView mDrawerLayoutListView;
    ActionBarDrawerToggle mActionBarDrawerToggle;

    // music player controller
    MusicPlayerController mMusicPlayerController;

    // database sync
    FileSearch mFileSearch;

    public static final int REQUEST_CODE_RESULT_LIST_ACTIVITY = 1;
    public static final int REQUEST_CODE_SEARCH_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up navigation drawer
        initializeDrawer(toolbar);

        // create music player controller
        mMusicPlayerController = (MusicPlayerController) findViewById(R.id.music_controller);

    }

    private void initializeDrawer(final Toolbar toolbar) {
        // get the drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // get the drawer list view
        mDrawerLayoutListView = (ListView) findViewById(R.id.left_drawer);

        // add drawer item to drawer item list view
        // create array
        DrawerItem[] drawerItems = new DrawerItem[3];
        // populate array
        drawerItems[0] = new DrawerItem(R.drawable.music_note_white, "Library");
        drawerItems[1] = new DrawerItem(R.drawable.search_white, "Search");
        drawerItems[2] = new DrawerItem(R.drawable.sync, "Sync");

        // create drawer adapter with the list of drawer items
        mDrawerAdapter = new DrawerAdapter(this, R.layout.drawer_item, drawerItems);
        // set the drawer adapter on the drawer
        mDrawerLayoutListView.setAdapter(mDrawerAdapter);

        // create item click listener
        ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
            // onClick for mDrawerLayout listView items
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch (position) {
                    case 0:
                        intent = new Intent(view.getContext(), ResultListsActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_RESULT_LIST_ACTIVITY);
                        break;
                    case 1:
                        intent = new Intent(view.getContext(), SearchActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SEARCH_ACTIVITY);
                        break;
                    case 2:
                        // sync database
                        syncDatabase();
                        break;
                }
                // close the drawer layout once an item is clicked
                mDrawerLayout.closeDrawers();
            }
        };
        // set this as an onClick listener for the drawer items
        mDrawerLayoutListView.setOnItemClickListener(onItemClickListener);

        // this is the animated toggle button which opens and closes the drawer
        // set action bar drawer toggle
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {
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
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        ActionBar actionBar = getSupportActionBar();
        // set back button state
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        // sync state
        mActionBarDrawerToggle.syncState();
    }

    private void syncDatabase() {

        if(mFileSearch == null) {
            // create new file search
            mFileSearch = new FileSearch(getBaseContext());
        }
        // run file search
        mFileSearch.doInBackground(null, null, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == REQUEST_CODE_RESULT_LIST_ACTIVITY && resultCode == Activity.RESULT_OK) {
            // get the array of media files from the intent
            Bundle bundle = intent.getBundleExtra(ResultListsActivity.RESULT_LIST_ACTIVITY_RESPONSE_KEY);
            Object[] ara = (Object[]) bundle.getSerializable(ResultListsActivity.RESULT_LIST_ACTIVITY_RESPONSE_KEY);
            // clear now playing list and add new items
            ArrayList<MediaFile> temp = new ArrayList<>();
            if(ara != null) {
                for (Object o : ara) {
                    temp.add((MediaFile) o);
                }
            }
            // set the now playing list in the music player
            mMusicPlayerController.setNowPlayingList(temp);
            mMusicPlayerController.start();
        }
        else if(requestCode == REQUEST_CODE_SEARCH_ACTIVITY && resultCode == Activity.RESULT_OK) {
            // get the media file from the intent
            Bundle bundle = intent.getBundleExtra(ResultListsActivity.RESULT_LIST_ACTIVITY_RESPONSE_KEY);
            MediaFile mediaFile = (MediaFile) bundle.getSerializable(SearchActivity.SEARCH_ACTIVITY_RESPONSE_KEY);
            // make new list of media file
            ArrayList<MediaFile> temp = new ArrayList<>();
            temp.add(mediaFile);
            // set the now playing list in the music player
            mMusicPlayerController.setNowPlayingList(temp);
            mMusicPlayerController.start();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLayoutListView);
        // if the drawer is open, hide the button settings
        menu.findItem(R.id.action_about).setVisible(!drawerOpen);
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
        if (id == R.id.action_about) {

            // show about dialog
            MusicPlayerDialog.aboutDialog(MainActivity.this);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
