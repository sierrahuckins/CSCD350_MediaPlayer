package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * MusicPlayer.java
 * Author: Bruce Emehiser
 * Date: 20151119
 * Description: Wrapper around android.media.MediaPlayer
 */
public class MusicPlayer {

    // song changed listener
    public interface OnMediaChangedListener {

        void songStarted(MusicPlayer musicPlayer);
        void songStopped(MusicPlayer musicPlayer);
        void songEnded(MusicPlayer musicPlayer);
    }

    // notification types
    private enum Notification {
        SONG_STARTED,
        SONG_STOPPED,
        SONG_ENDED
    }

    // array of listeners
    private ArrayList<OnMediaChangedListener> mListeners;

    // context of application
    private Context mContext;

    // instance of a media player
    private MediaPlayer mMediaPlayer;

    public MusicPlayer(Context context) {
        mContext = context;
        mListeners = new ArrayList<>();
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
        // media player is paused
        if(mMediaPlayer != null && ! mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();

            // notify listeners
            notifyListeners(Notification.SONG_STARTED);
        }
    }

    public void setNowPlaying(MediaFile nowPlaying) {

        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        // reset media player, prepare the media player and start it
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mContext, nowPlaying.getUri());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.e("MusicPlayer", "Error setting mediaplayer data source, Uri has problems");
        }

        // on completion of this song's playback
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                notifyListeners(Notification.SONG_ENDED);
            }
        });
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
            notifyListeners(Notification.SONG_STOPPED);
        }
    }

    public void stop() {
        if(mMediaPlayer != null) {
            if(mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            notifyListeners(Notification.SONG_STOPPED);
        }
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
            mListeners.add(listener);
        }
    }

    private void notifyListeners(Notification notification) {

        // update listeners
        switch (notification) {

            case SONG_STARTED:
                for (OnMediaChangedListener listener : mListeners) {
                    listener.songStarted(this);
                }
                break;
            case SONG_STOPPED:
                for (OnMediaChangedListener listener : mListeners) {
                    listener.songStopped(this);
                }
                break;
            case SONG_ENDED:
                for(OnMediaChangedListener listener : mListeners) {
                    listener.songEnded(this);
                }
        }
    }
}