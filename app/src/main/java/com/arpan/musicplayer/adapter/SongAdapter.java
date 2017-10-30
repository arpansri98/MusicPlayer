package com.arpan.musicplayer.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpan.musicplayer.R;
import com.arpan.musicplayer.activity.MainActivity;
import com.arpan.musicplayer.fragment.MusicControllerFragment;
import com.arpan.musicplayer.fragment.SongsListFragment;
import com.arpan.musicplayer.model.Song;
import com.arpan.musicplayer.rest.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.songsViewHolder>{

    private Context mContext;
    private ArrayList<Song> mSongs;
    private Activity mMainActivity;
    private MusicControllerFragment mMusicControllerFragment;
    private SongsListFragment mSongsListFragment;
    private int mPosition;

    public SongAdapter(Context context, Activity mainActivity, MusicControllerFragment musicControllerFragment, SongsListFragment songsListFragment, ArrayList<Song> songs) {
        mContext = context;
        mMainActivity = mainActivity;
        mMusicControllerFragment = musicControllerFragment;
        mSongsListFragment = songsListFragment;
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

        holder.bindViews(song.getSongName(), song.getArtistName(), song.getAlbumArtUri());
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    @Deprecated
    public Bitmap getBitmapFromId(long albumId) {

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        Bitmap bitmap = null;

        try {

            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), albumArtUri);
            if (bitmap != null) bitmap = Bitmap.createBitmap(bitmap);

        } catch (FileNotFoundException exception) {

            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.no_album_art);

            exception.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        if (bitmap == null) bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.no_album_art);

        return bitmap;
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

            //Making it movable // I guess it's waste :/
            songLabel.setMovementMethod(new ScrollingMovementMethod());
            itemView.setOnClickListener(this);

        }

        public void bindViews(String songName, String artistName, Uri albumArtUri) {
            songLabel.setText(songName);
            artistLabel.setText(artistName);

            GlideApp
                    .with(mContext)
                    .load(albumArtUri)
                    .placeholder(R.drawable.ic_play_circle_filled_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(albumArt);
        }

        @Override
        public void onClick(View view) {

            ((MainActivity) mMainActivity).setCurrentSongInService(mSongs.get(getAdapterPosition()));

            ((MainActivity) mMainActivity).setUriInService(mSongs.get(getAdapterPosition()).getUri());

            ((MainActivity) mMainActivity).setCallBackInService(mMusicControllerFragment);

            ((MainActivity) mMainActivity).playMusic(true);
            Log.d(TAG, "onClick: " + mSongs.get(getAdapterPosition()).getUri());

            //Set the CallBack in Service as MainActivity if Req.d

        }

    }

}
