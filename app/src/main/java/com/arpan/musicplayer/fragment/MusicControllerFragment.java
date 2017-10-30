package com.arpan.musicplayer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arpan.musicplayer.rest.GlideApp;
import com.arpan.musicplayer.service.PlayerService;
import com.arpan.musicplayer.R;
import com.arpan.musicplayer.model.Song;
import com.arpan.musicplayer.activity.MainActivity;


public class MusicControllerFragment extends Fragment implements PlayerService.ServiceCallbacks {

    public static final String TAG = MusicControllerFragment.class.getSimpleName();
    private Uri mArtworkUri = Uri.parse("content://media/external/audio/albumart");

    private ImageButton mPlayButton;
    private ImageButton mPauseButton;
    private SeekBar mSeekBar;
    private TextView mMaxLabel;
    private TextView mElapsedLabel;
    private ImageView mAlbumArtImageView;
    private TextView mArtistTitle;
    private TextView mAlbumTitle;
    private TextView mSongTitle;
    private PlayerService mPlayerService;

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
        mAlbumTitle = getActivity().findViewById(R.id.albumTitle);

        mSongTitle.setSelected(true);
        mAlbumTitle.setSelected(true);
        mArtistTitle.setSelected(true);

        mPlayerService = ((MainActivity) getActivity()).getPlayerServiceFromActivity();

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

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerService == null) {
                    mPlayerService = ((MainActivity) MusicControllerFragment.this.getActivity()).getPlayerServiceFromActivity();

                    if (mPlayerService.isPlaying()) {
                        mPlayerService.pauseMusic();
                        mPauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle_filled_black_24dp, null));
                    }

                    else if (!mPlayerService.isPlaying()) {
                        mPlayerService.resumeMusic();
                        mPauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_black_24dp, null));
                    }
                }
            }
        });


    }

    public void updateViews(Song song) {

        mSongTitle.setText(song.getSongName());
        mArtistTitle.setText(song.getArtistName());
        mAlbumTitle.setText(song.getAlbumName());

        GlideApp
                .with(this)
                .load(song.getAlbumArtUri())
                .placeholder(R.drawable.ic_play_circle_filled_black_24dp)
                .into(mAlbumArtImageView);
    }

    @Override
    public void updateViewsInControllerFragment(Song song) {

        updateViews(song);
    }

}
