package com.mediaplayer.tba.cscd350_mediaplayer;

import android.content.Context;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * MediaFileTest.java
 * Author: Bruce Emehiser
 * Date: 20151203
 * Description: Test of MediaFile Class
 */

@RunWith(MockitoJUnitRunner.class)
public class MediaFileTest {

    @Mock
    Context mMockContext;

    @Mock
    Uri mUri;

    /**
     * Check for null constructor parameters
     * @throws Exception
     */
    @Test
    public void MediaFile_NullParameter() throws Exception {

        try {
            MediaFile mediaFile = new MediaFile(null, null, null, null, null);
            fail("test should have thrown an exception");
        } catch (Throwable expected) {
            assertEquals(NullPointerException.class, expected.getClass());
        }
    }

    /**
     * Check for good parameters for working functionality
     * @throws Exception
     */
    @Test
    public void MediaFile_GoodParameter() throws Exception {
        MediaFile mediaFile = new MediaFile("Artist", "Album", "Title", "Genre", mUri);
    }
}