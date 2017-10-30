package com.arpan.musicplayer.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arpan.musicplayer.R;
import com.arpan.musicplayer.database.DatabaseHelper;
import com.arpan.musicplayer.model.Song;
import com.arpan.musicplayer.adapter.SongAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.arpan.musicplayer.database.DatabaseHelper.ALBUM_ART_URI;
import static com.arpan.musicplayer.database.DatabaseHelper.ALBUM_ID;
import static com.arpan.musicplayer.database.DatabaseHelper.ALBUM_NAME;
import static com.arpan.musicplayer.database.DatabaseHelper.ARTIST_NAME;
import static com.arpan.musicplayer.database.DatabaseHelper.SONG_DURATION;
import static com.arpan.musicplayer.database.DatabaseHelper.SONG_NAME;
import static com.arpan.musicplayer.database.DatabaseHelper.SONG_URI;
import static com.arpan.musicplayer.database.DatabaseHelper.TABLE_NAME;

public class SongsListFragment extends Fragment{

    private RecyclerView mSongsRecyclerView;

    private ArrayList<Song> mSongs = new ArrayList<>();
    private ProgressBar mProgressBar;

    //FIXME
//    public class CustomPreloadModelProvider implements ListPreloader.PreloadModelProvider {
//
//        @NonNull
//        @Override
//        public List getPreloadItems(int position) {
//
//
////            String url = mSongs.get(position).getUri();
//            if (TextUtils.isEmpty(url)) {
//                return Collections.emptyList();
//            }
//            return Collections.singletonList(url);
//        }
//
//        @Nullable
//        @Override
//        public RequestBuilder getPreloadRequestBuilder(Object item) {
//            return Glide.with(SongsListFragment.this).load(item);
//        }
//    }

    //Glide Vars
//    private ViewPreloadSizeProvider mSizeProvider = new ViewPreloadSizeProvider();
//    private CustomPreloadModelProvider mModelProvider = new CustomPreloadModelProvider();
//    RecyclerViewPreloader<Bitmap> mPreloader;

    //Custom DB Vars
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;

    //Shared Pref
    private SharedPreferences mSharedPreferences;
    private static final String FIRST_TIME = "FIRST_TIME";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_song_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSongsRecyclerView = getActivity().findViewById(R.id.songsRecyclerView);
        mProgressBar = getActivity().findViewById(R.id.loadingProgressBar);

//        mPreloader = new RecyclerViewPreloader<Bitmap>(Glide.with(this), mModelProvider, mSizeProvider, 2);

        mDatabaseHelper = new DatabaseHelper(getContext());

        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        //First Opening of the APP
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, true);
        editor.apply();

        LoadSongsTask task = new LoadSongsTask();
        task.execute();

    }

    private void loadSongsInCustomDB() {

        mDatabase = mDatabaseHelper.getWritableDatabase();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);

        if (mDatabase != null) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String songUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        int songDuration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        Long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.SONG_NAME, songName);
                        values.put(ARTIST_NAME, artistName);
                        values.put(SONG_URI, songUrl);
                        values.put(SONG_DURATION, songDuration);
                        values.put(ALBUM_NAME, albumName);
                        values.put(ALBUM_ID, albumId);
                        values.put(ALBUM_ART_URI, albumArtUri.toString());

                        mDatabase.beginTransaction();
                        mDatabase.insert(TABLE_NAME, null, values);
                        mDatabase.setTransactionSuccessful();
                        mDatabase.endTransaction();

                        //Song song = new Song(name, albumName, albumId, albumArtUri, artistName, url, duration);
                        //Instead, put the values in ContentValues values = new ContentValues() Obj.
                        //mSongs.add(song);

                    } while (cursor.moveToNext());

                    mDatabase.close();
                    cursor.close();
                }
            }
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, false);
        editor.apply();
    }

//    private void sortSongs() {
//
//        Collections.sort(mSongs, new Comparator<Song>() {
//            @Override
//            public int compare(Song song1, Song song2) {
//                return song1.getSongName().compareTo(song2.getSongName());
//            }
//        });
//    }

    private void setUpRecyclerView() {

        SongAdapter adapter = new SongAdapter(
                getContext(),
                getActivity(),
                (MusicControllerFragment) getFragmentManager().getFragments().get(1),
                SongsListFragment.this,
                mSongs
                );

        mSongsRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSongsRecyclerView.setLayoutManager(layoutManager);
        mSongsRecyclerView.setHasFixedSize(true);

//        mSongsRecyclerView.addOnScrollListener(mPreloader);
    }

    // COMPLETED ADD AN ASYNC TASK :/
    // COMPLETED LET'S TRY :o
    // COMPLETED I THINK IT'S DONE :)

    class LoadSongsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(true);
            mSongsRecyclerView.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Loading Songs", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mSharedPreferences.getBoolean(FIRST_TIME, false)) loadSongsInCustomDB();
            loadSongsInArrayList();
//            sortSongs();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.GONE);
            mSongsRecyclerView.setVisibility(View.VISIBLE);

            setUpRecyclerView();
        }


    }

    private void loadSongsInArrayList() {

        mDatabase = mDatabaseHelper.getReadableDatabase();

        Cursor cursor = mDatabase.query(
                TABLE_NAME,
                new String[] {SONG_NAME, ARTIST_NAME, SONG_URI, SONG_DURATION, ALBUM_NAME, ALBUM_ID, ALBUM_ART_URI},
                null,
                null,
                null,
                null,
                null
                );

        if (mDatabase != null) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String songName = cursor.getString(cursor.getColumnIndex(SONG_NAME));
                        String artistName = cursor.getString(cursor.getColumnIndex(ARTIST_NAME));
                        String songUrl = cursor.getString(cursor.getColumnIndex(SONG_URI));
                        int songDuration = cursor.getInt(cursor.getColumnIndex(SONG_DURATION));
                        String albumName = cursor.getString(cursor.getColumnIndex(ALBUM_NAME));
                        Long albumId = cursor.getLong(cursor.getColumnIndex(ALBUM_ID));
                        Uri albumArtUri = Uri.parse(cursor.getString(cursor.getColumnIndex(ALBUM_ART_URI)));

                        Song song = new Song(songName, albumName, albumId, albumArtUri, artistName, songUrl, songDuration);
                        //Instead, put the values in ContentValues values = new ContentValues() Obj.
                        mSongs.add(song);

                    } while (cursor.moveToNext());

                    mDatabase.close();
                    cursor.close();
                }
            }
        }
    }
}

//FIXME ADD TO DB ONLY ON FIRST RUN, AFTER THAT LOAD ONLY FROM DB AND FOR FILE CHANGES ADD TO DB THEN TO UI //
