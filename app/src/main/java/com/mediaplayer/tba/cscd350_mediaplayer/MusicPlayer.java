package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bruce Emehiser on 11/19/2015.
 *
 * Wrapper around MediaPlayer
 */
public class MusicPlayer {

    // song changed listener
    public interface OnMediaChangedListener {

        void songChanged(MusicPlayer musicPlayer);
        void songStopped(MusicPlayer musicPlayer);
        void songStarted(MusicPlayer musicPlayer);
    }

    // notification types
    private enum Notification {
        SONG_CHANGED,
        SONG_PAUSED,
        SONG_RESUMED
    }

    // array of listeners
    private ArrayList<OnMediaChangedListener> listeners;

    // context of application
    private Context mContext;

    // instance of a media player
    private MediaPlayer mMediaPlayer;

    // now playing list
    private ArrayList<MediaFile> mNowPlaying;
    private int mNowPlayingPosition;

    public MusicPlayer(Context context) {
        mContext = context;
        mNowPlaying = new ArrayList<>();
        mNowPlayingPosition = 0;
        listeners = new ArrayList<>();
    }

    public void setNowPlaying(final ArrayList<MediaFile> nowPlaying) {
        // set the now playing list
        if(nowPlaying != null) {
            mNowPlaying = nowPlaying;
            mNowPlayingPosition = 0;
        }
    }

    public String getCurrentlyPlaying() {
        // get the song that is currently playing or paused
        if(mNowPlaying != null && mNowPlaying.size() > 0 && mNowPlayingPosition < mNowPlaying.size()) {
            return mNowPlaying.get(mNowPlayingPosition).getTitle();
        }
        return "";
    }

    public void setLooping(boolean loop) {
        if(mMediaPlayer != null) {
            mMediaPlayer.setLooping(loop);
        }
    }

    public boolean getLoopSong() {
        return mMediaPlayer != null && mMediaPlayer.isLooping();
    }

    public void start() {
        // media player is stopped
        if(mMediaPlayer == null) {
            begin();
        }
        // media player is paused
        else if(! mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();

            // notify listeners
            notifyListeners(Notification.SONG_RESUMED);
        }
    }

    public void next() {
        // check for empty now playing list
        if(mNowPlaying.isEmpty()) {
            return;
        }
        // go to next position
        mNowPlayingPosition++;
        // if we are before the end of the now playing list
        if (mNowPlayingPosition < mNowPlaying.size()) {
            // begin playback at position
            begin();
        }
        else {
            // reset the now playing position to now playing beginning
            mNowPlayingPosition = 0;
        }
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
    }

    private void begin() {

        // check for null now playing list
        if(mNowPlaying == null) {
            return;
        }
        // check for empty now playing list
        if(mNowPlaying.size() == 0) {
            return;
        }
        // check for null player
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        // get next media file
        MediaFile mediaFile = mNowPlaying.get(mNowPlayingPosition);

        // reset media player, prepare the media player and start it
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mContext, mediaFile.getUri());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.e("IOException", "Cannot play media file," + mediaFile.toString() + " Invalid Uri (or something like that)");
            return;
        }
        // on completion of this song's playback
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // if we are looping
                if(mMediaPlayer.isLooping()) {
                    // begin current song
                    begin();
                }
                else {
                    // play the next song
                    next();
                }
            }
        });
        // start the song playing
        mMediaPlayer.start();

        // notify listeners
        notifyListeners(Notification.SONG_CHANGED);
    }

    public void pause() {
        // check for null
        if(mMediaPlayer == null) {
            return;
        }
        // if media player is playing, pause it
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();

            // notify listeners
            notifyListeners(Notification.SONG_PAUSED);
        }
    }

    public void stop() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public boolean isPlaying() {

        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public int getDuration() {
        if(mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if(mMediaPlayer != null) {
            return  mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if(mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
        }
    }

    public void setOnMediaChangedListener(OnMediaChangedListener listener) {
        if(listener != null) {
            listeners.add(listener);
        }
    }

    private void notifyListeners(Notification notification) {

        // update listeners
        switch (notification) {

            case SONG_CHANGED:
                for (OnMediaChangedListener listener : listeners) {
                    listener.songChanged(this);
                }
                break;
            case SONG_PAUSED:
                for (OnMediaChangedListener listener : listeners) {
                    listener.songStopped(this);
                }
                break;
            case SONG_RESUMED:
                for (OnMediaChangedListener listener : listeners) {
                    listener.songStarted(this);
                }
                break;
        }
    }
}