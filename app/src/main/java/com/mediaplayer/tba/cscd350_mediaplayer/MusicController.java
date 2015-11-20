package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * Created by Bruce Emehiser on 11/19/2015.
 *
 */
public class MusicController extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    // activity context
    Context mContext;

    // music player to control
    MusicPlayer mMusicPlayer;

    // buttons
    ImageButton mPlayPauseButton;
    ImageButton mPrevButton;
    ImageButton mNextButton;
    ImageButton mRepeatButton;

    SeekBar mSeekBar;

    public MusicController(Context context) {
        super(context);
        mContext = context;
        intialize();
    }

    public MusicController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        intialize();
    }

    public MusicController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        intialize();
    }

    private void intialize() {
        // inflate the custom view and hook it to the view group
        LayoutInflater  inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_controller, this, true);

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

        // set seek bar
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        if(musicPlayer != null) {
            mMusicPlayer = musicPlayer;
        }
    }

    public int getCurrentPosition() {
        return 0;
    }

    public void seekTo(int pos) {

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
                }
                else {
                    mPlayPauseButton.setImageResource(R.drawable.play);
                }
                break;
            case R.id.next_button:
                mMusicPlayer.next();
                break;
            case R.id.prev_button:
                mMusicPlayer.prev();
                break;
            case R.id.repeat_button:
                // change the media player looping
                mMusicPlayer.setLoopSong(!mMusicPlayer.getLoopSong());
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
        // TODO: 11/20/2015 set runnable thread to update seek bar based on media player position
        // TODO: 11/20/2015 set listener to listen for seek bar change
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max
     *                 was set by {link ProgressBar#setMax(int)}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // TODO: 11/19/2015 create private inner async task class, and use it to updated seek bar
        // as shown http://stackoverflow.com/questions/25232343/android-async-task-progress-bar-onprogressupdate

    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
