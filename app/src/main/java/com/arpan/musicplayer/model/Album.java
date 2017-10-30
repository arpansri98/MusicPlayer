package com.arpan.musicplayer.model;

import android.net.Uri;

import java.util.ArrayList;

class Album {

    private String mAlbumName;
    private ArrayList<Song> mSongs;
    private Uri mAlbumUri;

    public Album(String albumName, ArrayList<Song> songs, Uri albumUri) {
        mSongs = songs;
        mAlbumName = albumName;
        mAlbumUri = albumUri;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public ArrayList<Song> getSongs() {
        return mSongs;
    }
}
