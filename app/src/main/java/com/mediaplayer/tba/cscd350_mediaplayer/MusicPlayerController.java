package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * MusicPlayerController.java
 * Author: Bruce Emehiser
 * Date: 20151119
 * Description: Custom view used to control Music player
 */
public class MusicPlayerController extends LinearLayout implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, MusicPlayer.OnMediaChangedListener {

    // logcat tag
    public static final String TAG = "MusicPlayerController";

    // activity context
    private Context mContext;

    // music player to control
    private MusicPlayer mMusicPlayer;

    // controls
    private ImageButton mPlayPauseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private ImageButton mRepeatButton;

    private SeekBar mSeekBar;

    private TextView mNowPlayingTextView;

    // now playing media files
    ArrayList<MediaFile> mNowPlayingList;
    private int mNowPlayingPosition;

    // now playing view
    ArrayAdapter<MediaFile> mNowPlayingAdapter;
    ListView mNowPlayingListView;

    // flags
    boolean mDragging;
    static final int SHOW_PROGRESS = 1;

    public MusicPlayerController(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    public MusicPlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initialize();
    }

    public MusicPlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initialize();
    }

    private void initialize() {
        // inflate the custom view and hook it to the view group
        LayoutInflater  inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_player_controller, this, true);

        // set music player
        mMusicPlayer = new MusicPlayer(mContext);
        mMusicPlayer.setOnMediaChangedListener(this);

        // controls
        mPlayPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mRepeatButton = (ImageButton) findViewById(R.id.repeat_button);

        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        // set listeners
        mPlayPauseButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);

        // set seek bar changed listener
        mSeekBar.setOnSeekBarChangeListener(this);

        // initialize currently playing
        mNowPlayingTextView = (TextView) findViewById(R.id.now_playing_text_view);

        // initialize now playing
        mNowPlayingList = new ArrayList<>();
        mNowPlayingPosition = 0;

        // get new adapter and pass it a list item layout and the text view in the list item
        mNowPlayingAdapter = new ArrayAdapter<>(mContext, R.layout.list_item, R.id.list_entry, mNowPlayingList);
        // set the adapter on the list
        mNowPlayingListView = (ListView) findViewById(R.id.now_playing_list);
        mNowPlayingListView.setAdapter(mNowPlayingAdapter);
        // set on click listeners
        mNowPlayingListView.setOnItemLongClickListener(MusicPlayerController.this);
        mNowPlayingListView.setOnItemClickListener(MusicPlayerController.this);
    }

    public void setNowPlayingList(ArrayList<MediaFile> nowPlayingList) {
        if(nowPlayingList != null) {
            // set list
            mNowPlayingList.clear();
            mNowPlayingList.addAll(nowPlayingList);
            // set the position
            mNowPlayingPosition = 0;
            // update the now playing view
            mNowPlayingAdapter.notifyDataSetChanged();
            // start playing
            begin();
            // update gui
            nowPlayingChanged();
        }
    }

    public void addToNowPlayingList(ArrayList<MediaFile> nowPlayingList) {
        if(nowPlayingList != null) {
            try {
                mNowPlayingList.addAll(nowPlayingList);
                mNowPlayingAdapter.notifyDataSetChanged();
            }catch (NullPointerException e) {
                Log.e(TAG, "Error adding files to now playing list");
            }
        }
    }

    public void start() {
        // begin playing music
        begin();
    }

    public void stop() {
        // stop the music player
        mMusicPlayer.stop();
    }

    private void begin() {
        // if list not null or empty, and now playing position is within the bounds of the now playing list
        if(mNowPlayingList != null && !mNowPlayingList.isEmpty() && mNowPlayingList.size() > mNowPlayingPosition) {
            // clear the music player
            mMusicPlayer.stop();
            // set the media player
            mMusicPlayer.setNowPlaying(mNowPlayingList.get(mNowPlayingPosition));
            // star the music player
            mMusicPlayer.start();
        }
        // update the now playing view and seek bar
        nowPlayingChanged();
    }

    public void next() {
        // check for empty now playing list
        if(mNowPlayingList.isEmpty()) {
            return;
        }
        // go to next position
        mNowPlayingPosition++;
        // if we are before the end of the now playing list
        if (mNowPlayingPosition < mNowPlayingList.size() && mNowPlayingPosition >= 0) {
            // begin playback at position
            begin();
        }
        else {
            // reset the now playing position to now playing beginning
            mNowPlayingPosition = 0;
        }
        // update the now playing view and seek bar
        nowPlayingChanged();
    }
    public void prev() {
        // go to previous position
        mNowPlayingPosition --;
        // if we are the first song
        if(mNowPlayingPosition < 0) {
            mNowPlayingPosition = 0;
        }
        // begin playing at position
        begin();
        // update the now playing view and seek bar
        nowPlayingChanged();
    }


    @Override
    public void onClick(View view) {

        if(mMusicPlayer == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.play_pause_button:
                if(mMusicPlayer.isPlaying()) {
                    // pause music player
                    mMusicPlayer.pause();
                }
                else {
                    // start music player
                    mMusicPlayer.start();
                }
                // set play pause button
                if(mMusicPlayer.isPlaying()) {
                    mPlayPauseButton.setImageResource(R.drawable.pause);
                    mHandler.sendEmptyMessage(SHOW_PROGRESS);
                }
                else {
                    mPlayPauseButton.setImageResource(R.drawable.play);
                    mHandler.removeMessages(SHOW_PROGRESS);
                }
                break;
            case R.id.next_button:
                next();
                break;
            case R.id.prev_button:
                prev();
                break;
            case R.id.repeat_button:
                // change the media player looping
                mMusicPlayer.setLooping(!mMusicPlayer.getLoopSong());
                // change repeat icon
                if(mMusicPlayer.getLoopSong()) {
                    mRepeatButton.setImageResource(R.drawable.repeat_on);
                }
                else {
                    mRepeatButton.setImageResource(R.drawable.repeat_off);
                }
                break;
        }

        // set the seek bar max position
        mSeekBar.setMax(mMusicPlayer.getDuration());
        // set the current playback position
        mSeekBar.setProgress(mMusicPlayer.getCurrentPosition());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser) {
            // set the music based on the seek bar position
            mMusicPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // set handler dragging flag
        mDragging = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // set handler dragging flag
        mDragging = false;

    }

    private void nowPlayingChanged() {
        // update the max seek bar size
        mSeekBar.setMax(mMusicPlayer.getDuration());
        // update the now playing list string
        String nowPlayingString = mNowPlayingList.size() > mNowPlayingPosition ? mNowPlayingList.get(mNowPlayingPosition).getTitle() : "";
        // change the now playing string
        mNowPlayingTextView.setText(String.format("%s: %s", mContext.getString(R.string.music_player_controller_now_playing), nowPlayingString));
        // change repeat icon
        if(mMusicPlayer.getLoopSong()) {
            mRepeatButton.setImageResource(R.drawable.repeat_on);
        }
        else {
            mRepeatButton.setImageResource(R.drawable.repeat_off);
        }
    }

    @Override
    public void songStarted(MusicPlayer musicPlayer) {
        // update the play pause button
        ImageButton playPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        playPauseButton.setImageResource(R.drawable.pause);

        // start updating the seek bar
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        // update now playing text
        nowPlayingChanged();
    }
    @Override
    public void songStopped(MusicPlayer musicPlayer) {

        // update play pause button
        ImageButton playPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        playPauseButton.setImageResource(R.drawable.play);

        // stop updating the seek bar
        mHandler.removeMessages(SHOW_PROGRESS);

    }

    @Override
    public void songEnded(MusicPlayer musicPlayer) {

        // if we have more items to play
        if(mNowPlayingList != null && mNowPlayingList.size() > mNowPlayingPosition) {
            // play next
            next();
        }
        // else, stop the player
        else {
            mHandler.removeMessages(SHOW_PROGRESS);
        }
        // update now playing list
        nowPlayingChanged();
    }

    /**
     * This block of code will stop being called when the media player stops.
     * Because , the handler and the activity
     * it is attached to will be garbage collected
     */
    @SuppressWarnings("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            if(mMusicPlayer.isPlaying()) {
                pos = mMusicPlayer.getCurrentPosition();
                if(! mDragging) {
                    // update seek bar
                    mSeekBar.setProgress(pos);
                }
                // clear and set next message and delay
                msg = obtainMessage(SHOW_PROGRESS);
                mHandler.sendMessageDelayed(msg, 1000 - (pos % 1000));
            }
            else {
                // clear messages
                removeMessages(SHOW_PROGRESS);
            }
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // get database
        LibraryDatabase db = new LibraryDatabase(getContext());
        // get string representation of playlist title
        MediaFile[] mediaFiles = new MediaFile[] {mNowPlayingList.get(position)};
        // prompt to select playlist
        MusicPlayerDialog.selectPlaylistDialog(mContext, db, mediaFiles);

        // return we handled the event
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // if the chosen item is in our list
        if(position < mNowPlayingList.size() && position >= 0) {
            // stop player
            mMusicPlayer.stop();
            // move to position
            mNowPlayingPosition = position;
            // play that item
            begin();
        }
    }
}
