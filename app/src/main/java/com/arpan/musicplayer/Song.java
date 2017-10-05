package com.arpan.musicplayer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.net.URL;

public class Song {

    private Bitmap mAlbumArt;
    private String mSongName;
    private String mArtistName;
    private String mAlbumName;
    private String mFilePath;
    private String mUri;
    private int mDuration;

    public Song(Bitmap albumArt, String songName, String artistName, String uri) {

        mAlbumArt = albumArt;
        mSongName = songName;
        mArtistName = artistName;
        mUri = uri;
    }


    public Song(Bitmap albumArt, String songName, String artistName, String albumName, String uri) {

        mAlbumArt = albumArt;
        mSongName = songName;
        mArtistName = artistName;
        mAlbumName = albumName;
        mUri = uri;
    }

    public Bitmap getAlbumArt() {
        return mAlbumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        mAlbumArt = albumArt;
    }

    public String getSongName() {
        return mSongName;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getUri() {
        return mUri;
    }
}
