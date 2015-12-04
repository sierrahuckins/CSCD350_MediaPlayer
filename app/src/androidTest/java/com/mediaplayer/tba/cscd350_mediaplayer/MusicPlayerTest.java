package com.mediaplayer.tba.cscd350_mediaplayer;

import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Bruce Emehiser on 12/3/2015.
 *
 * Test for MusicPlayer.java
 */
public class MusicPlayerTest {

    MockContext mMockContext;

    MusicPlayer mMusicPlayer;

    @Before
    public void setUp() throws Exception {

        mMockContext = new MockContext();

        mMusicPlayer = new MusicPlayer(mMockContext);

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
    public void testSetNowPlaying() throws Exception {

    }

    @Test
    public void testPause() throws Exception {

    }

    @Test
    public void testStop() throws Exception {

    }

    @Test
    public void testSeekTo() throws Exception {

    }

    @Test
    public void testSetOnMediaChangedListener() throws Exception {

    }
}