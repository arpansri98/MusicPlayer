package com.arpan.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.songsViewHolder>{

    private Context mContext;
    private ArrayList<Song> mSongs;
    private Activity mMainActivity;
    private int mPosition;

    public SongAdapter(Context context, Activity mainActivity, ArrayList<Song> songs) {
        mContext = context;
        mMainActivity = mainActivity;
        mSongs = songs;
    }

    @Override
    public songsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.songs_list_item, parent, false);
        songsViewHolder viewHolder = new songsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(songsViewHolder holder, int position) {

        mPosition = position;
        Song song = mSongs.get(position);
        holder.bindViews(song.getSongName(), song.getArtistName());

    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class songsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView songLabel;
        public TextView artistLabel;
        public ImageView albumArt;
        public Song mSong;

        public songsViewHolder(View itemView) {
            super(itemView);

            songLabel = itemView.findViewById(R.id.songNameLabel);
            artistLabel = itemView.findViewById(R.id.artistNameLabel);
            albumArt = itemView.findViewById(R.id.songAlbumArt);

            itemView.setOnClickListener(this);

        }

        public void bindViews(String songName, String artistName) {
            songLabel.setText(songName);
            artistLabel.setText(artistName);
            //FIXME Album Art Unset
        }

        @Override
        public void onClick(View view) {

            ((MainActivity) mMainActivity).setUriInService(mSongs.get(getAdapterPosition()).getUri());

            ((MainActivity) mMainActivity).playMusic(true);
                Log.d(TAG, "onClick: " + mSongs.get(getAdapterPosition()).getUri());
//            mMainActivity.playMusic();

            ((MainActivity) mMainActivity).setCurrentSongInService(mSongs.get(getAdapterPosition()));
//           ((MainActivity) mMainActivity).setSeekBarProgress();

//            mPlayerService.setUri(mSongs.get(mPosition).getUri());

        }
    }

}
