package com.arpan.musicplayer.model;

import java.util.ArrayList;

public class Artist {

    private String mArtistName;
    private ArrayList<Song> mSongs;
    private ArrayList<Album> mAlbums;


    public Artist(String artistName, ArrayList<Song> songs, ArrayList<Album> albums) {
        mArtistName = artistName;
        mSongs = songs;
        mAlbums = albums;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public ArrayList<Song> getSongs() {
        return mSongs;
    }

    public ArrayList<Album> getAlbums() {
        return mAlbums;
    }
}
