package com.mediaplayer.tba.cscd350_mediaplayer;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Andrew Macy on 12/6/2015.
 * Couldn't get the tests to work do to mocking issues
 */

@RunWith(MockitoJUnitRunner.class)
public class LibraryDatabaseTest extends AndroidTestCase {

    @Mock
    private LibraryDatabase myDB;

    @Override
    @Before
    public void setUp(){
        myDB = new LibraryDatabase(new MockContext());


        Uri testUri = Uri.parse("fakeUri1");
        MediaFile testFile = new MediaFile("artist1", "album1","title1", "genre1", testUri);
        myDB.addMediaFile(testFile);
        myDB.addFileToPlaylist("playlist1", testFile);

        testUri = Uri.parse("fakeUri2");
        testFile = new MediaFile("artist2", "album2", "title2", "genre3", testUri);
        myDB.addMediaFile(testFile);

        testUri = Uri.parse("fakeUri3");
        testFile = new MediaFile("artist3", "album3", "title3", "genre3", testUri);
        myDB.addMediaFile(testFile);
        myDB.addFileToPlaylist("playlist1", testFile);
    }


    @Test
    public void test_populateDatabase(){
        Uri testUri = Uri.parse("fakeUri4");
        MediaFile testFile = new MediaFile("artist4", "album4","title4", "genre4", testUri);
        MediaFile[] files = {testFile};
        myDB.populateDatabase(files);

        testFile = myDB.getMediaFile("fakeUri4");

        assertEquals("title4", testFile.getTitle());
    }

    @Test
    public void test_addMediaFile(){
        Uri testUri = Uri.parse("fakeUri5");
        MediaFile testFile = new MediaFile("artist5", "album5","title5", "genre5", testUri);
        myDB.addMediaFile(testFile);

        testFile = myDB.getMediaFile("fakeUri5");

        assertEquals("artist5", testFile.getArtist());
    }

    @Test
    public void test_addFileToPlaylist(){
        MediaFile testFile = myDB.getMediaFile("fakeUri2");
        myDB.addFileToPlaylist("playlist2", testFile);

        String[] playlists = myDB.getPlaylists();

        assertEquals("playlist2", playlists[2]);

        String[] songs = myDB.getSongsFromPlaylist("playlist2");

        assertEquals("title2", songs[0]);

    }

    @Test
    public void tets_removeFromLibrary(){

    }

    @Test
    public void test_removeFromPlaylist(){

    }

    @Test
    public void test_getMediaFile(){
        MediaFile file = myDB.getMediaFile("fakeUri1");
        assertEquals("title1", file.getTitle());
    }

    @Test
    public void test_getMediaFiles(){
        MediaFile[] files = myDB.getMediaFiles();
        assertEquals("title1", files[0].getTitle());
        assertEquals("album2", files[1].getAlbum());
        assertEquals("artist3", files[2].getArtist());
    }

    @Test
    public void test_getMediaFilesFromAlbum(){
        MediaFile[] files = myDB.getMediaFilesFromAlbum("album1");
        assertEquals("title1", files[0].getTitle());
    }

    @Test
    public void test_getMediaFilesFromArtist(){
        MediaFile[] files = myDB.getMediaFilesFromArtist("artist2");
        assertEquals("album2", files[0].getAlbum());
    }

    @Test
    public void test_getMediaFilesFromPlaylist(){
        MediaFile[] files = myDB.getMediaFilesFromPlaylist("playlist1");
        assertEquals("album1", files[0].getAlbum());
        assertEquals("album3", files[1].getAlbum());
    }

    @Test
    public void test_getMediaFilesFromGenre(){
        MediaFile[] files = myDB.getMediaFilesFromGenre("genre2");
        assertEquals("artist2", files[0].getArtist());
    }

    @Test
    public void test_getPlaylists(){
        String[] playlists = myDB.getPlaylists();
        assertEquals("playlist1", playlists[0]);
    }

    @Test
    public void test_getArtists(){
        String[] artists = myDB.getArtists();
        assertEquals("artist1", artists[0]);
        assertEquals("artist2", artists[1]);
    }

    @Test
    public void test_getAlbums(){

    }

    @Test
    public void test_getGenres(){

    }

    @Test
    public void test_getSongs(){

    }

    @Test
    public void test_getSongTitles(){

    }

    @Test
    public void test_getSongsFromPlaylist(){

    }

    @Test
    public void test_getAlbumsFromArtist() {

    }

    @Test
    public void test_getSongsFromAlbum(){

    }

    @Test
    public void test_search(){

    }

}