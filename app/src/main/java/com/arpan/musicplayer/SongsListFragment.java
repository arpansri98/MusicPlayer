package com.arpan.musicplayer;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;

public class SongsListFragment extends Fragment{

    private RecyclerView mSongsRecyclerView;
    private ArrayList<Song> mSongs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_song_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSongsRecyclerView = getActivity().findViewById(R.id.songsRecyclerView);
        loadSongs();

        SongAdapter adapter = new SongAdapter(getContext(), getActivity(), mSongs);

        mSongsRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSongsRecyclerView.setLayoutManager(layoutManager);

    }

    private void loadSongs() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {

                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    Long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    Bitmap bitmap = null;

                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), albumArtUri);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);

                    } catch (FileNotFoundException exception) {
                        //FIXME
                        bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                                R.drawable.ic_play_circle_filled_black_24dp);

                        exception.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }



                    Log.d(TAG, "loadSongs: Duration of cursor-> " + duration);

                    Song song = new Song(bitmap, name, artistName, url);

                    mSongs.add(song);
                } while (cursor.moveToNext());

                cursor.close();

            }

        }

    }
}
