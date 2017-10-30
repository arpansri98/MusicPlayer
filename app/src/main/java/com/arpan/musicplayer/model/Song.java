package com.arpan.musicplayer.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.net.URL;

public class Song {

    private String mSongName;
    private String mArtistName;
    private long mAlbumId;
    private Uri mAlbumArtUri;
    private String mAlbumName;
    private String mFilePath;
    private String mUri;
    private int mDuration;

    public Song(String name, String albumName, long albumId, Uri albumArtUri, String artistName, String url, int duration) {

        mSongName = name;
        mAlbumId = albumId;
        mAlbumArtUri = albumArtUri;
        mArtistName = artistName;
        mAlbumName = albumName;
        mUri = url;
        mDuration = duration;        
    }

    public String getSongName() {
        return mSongName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public String getUri() {
        return mUri;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getAlbumName() { return  mAlbumName; }

    public long getAlbumId() {
        return mAlbumId;
    }

    public Uri getAlbumArtUri() {
        return mAlbumArtUri;
    }
}
