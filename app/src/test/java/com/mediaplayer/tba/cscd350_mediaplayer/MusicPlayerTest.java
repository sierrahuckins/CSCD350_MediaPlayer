package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


/**
 * MusicPlayerTest.java
 * Author: Bruce Emehiser
 * Date: 20151203
 * Description: Test of MusicPlayer Class
 */

@RunWith(MockitoJUnitRunner.class)
public class MusicPlayerTest extends AndroidTestCase {

    @Mock
    Context mMockContext;

    @Mock
    Uri mMockUri;

    MusicPlayer mMusicPlayer;

    ArrayList<MediaFile> mNowPlaying;

    MediaFile mMediaFile;



    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSeekTo_NoSong() throws Exception {

        mMusicPlayer = new MusicPlayer(mMockContext);

        // set up object
        mMediaFile = new MediaFile("Artist", "Album", "Title", "Genre", mMockUri);

        // set the media file
        mMusicPlayer.setNowPlaying(mMediaFile);

        assertEquals(mMusicPlayer.getCurrentPosition(), 1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSetLooping_True() throws Exception {
    }

    @Test
    public void testStart() throws Exception {

    }

    @Test
    public void testSetNowPlaying_Null() throws Exception {

    }

    @Test
    public void testPause() throws Exception {

    }

    @Test
    public void testStop() throws Exception {

    }

    @Test
    public void testSetOnMediaChangedListener() throws Exception {

    }
}