package com.arpan.musicplayer.fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arpan.musicplayer.R;
import com.arpan.musicplayer.adapter.ArtistListAdapter;
import com.arpan.musicplayer.model.Artist;
import com.arpan.musicplayer.model.Song;

import java.util.ArrayList;

public class ArtistListFragment extends Fragment {

    private ArrayList<Artist> mArtistsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_artist_list, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ArtistListAdapter adapter = new ArtistListAdapter(getContext(), mArtistsList);
    }

    private void loadArtistsList() {


    }
}
