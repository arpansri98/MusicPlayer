package com.arpan.musicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MusicControllerFragment extends Fragment implements PlayerService.ServiceCallbacks {

    private ImageButton mPlayButton;
    private ImageButton mPauseButton;
    private SeekBar mSeekBar;
    private TextView mMaxLabel;
    private TextView mElapsedLabel;
    private ImageView mAlbumArtImageView;
    private TextView mArtistTitle;
    private TextView mSongTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_music_controller, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mPlayButton = getActivity().findViewById(R.id.playButton);
        mPauseButton = getActivity().findViewById(R.id.pauseButton);
        mSeekBar = getActivity().findViewById(R.id.seekBar);
        mMaxLabel = getActivity().findViewById(R.id.maxTimeTextView);
        mElapsedLabel = getActivity().findViewById(R.id.elapsedTimeTextView);
        mAlbumArtImageView = getActivity().findViewById(R.id.albumArtImageView);
        mArtistTitle = getActivity().findViewById(R.id.artistTitle);
        mSongTitle = getActivity().findViewById(R.id.songTitle);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getContext(), "onProgressBarChanged: " + i, Toast.LENGTH_SHORT).show();

                if (fromUser) ((MainActivity) getActivity()).setMediaPlayerProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void updateViews(Song song) {

        mSongTitle.setText(song.getSongName());
        //TODO ADD ALBUM UPDATE
        mAlbumArtImageView.setImageBitmap(song.getAlbumArt());
    }

    @Override
    public void doSomething() {

    }

    @Override
    public void updateViewsInControllerFragment(Song song) {

    }
}
