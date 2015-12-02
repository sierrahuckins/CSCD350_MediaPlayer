package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Bruce Emehiser on 11/19/2015.
 *
 * Custom view used to control a Music Player
 */
public class MusicPlayerController extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MusicPlayer.OnMediaChangedListener {

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

    private TextView mNowPlaying;

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

        // controls
        mPlayPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mRepeatButton = (ImageButton) findViewById(R.id.repeat_button);

        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        mNowPlaying = (TextView) findViewById(R.id.now_playing_text_view);

        // set listeners
        mPlayPauseButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mRepeatButton.setOnClickListener(this);

        // set seek bar changed listener
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        if(musicPlayer != null) {
            mMusicPlayer = musicPlayer;
            // register as listener for changes
            mMusicPlayer.setOnMediaChangedListener(this);
        }
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

    @Override
    public void songChanged(MusicPlayer musicPlayer) {
        // update the max seek bar size
        mSeekBar.setMax(mMusicPlayer.getDuration());
        // change the currently playing song view
        mNowPlaying.setText(String.format("%s: %s", mContext.getString(R.string.music_player_controller_now_playing), musicPlayer.getCurrentlyPlaying()));

        // clear and restart the seek bar updater
        songStopped(musicPlayer);
        songStarted(musicPlayer);
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
    public void songStarted(MusicPlayer musicPlayer) {
        // update the play pause button
        ImageButton playPauseButton = (ImageButton) findViewById(R.id.play_pause_button);
        playPauseButton.setImageResource(R.drawable.pause);

        // start updating the seek bar
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
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
}
