package com.arpan.musicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity implements PlayerService.ServiceCallbacks{

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String SONG_PATH = Environment.getExternalStorageDirectory().getPath() + "/Take.mp3";

    private Button mButton;
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;
    private SeekBar mSeekBar;
    private TextView mMaxLabel;
    private TextView mElapsedLabel;
    private TextView mSongTitle;
    private TextView mAlbumTitle;
    private TextView mArtistTitle;
    private ImageView mAlbumArtImageView;

    private boolean mBound = false;
    private PlayerService mPlayerService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: Service connected successfully");

            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) iBinder;
            mPlayerService = binder.getService();
            
//            mPlayerService.setCallback(MainActivity.this);
            setCallBackInService(MainActivity.this);

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: ");

            mBound = false;
        }
    };


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent  = new Intent(MainActivity.this, PlayerService.class);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);

        initViews();

        loadFragments();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

     //   setSeekBarListener();
    }


    private void initViews() {

        mSeekBar = findViewById(R.id.seekBar);
        mButton = findViewById(R.id.playButton);
        mRecyclerView = findViewById(R.id.songsRecyclerView);
        mViewPager = findViewById(R.id.viewPager);
        mMaxLabel = findViewById(R.id.maxTimeTextView);
        mElapsedLabel = findViewById(R.id.elapsedTimeTextView);
        mSongTitle = findViewById(R.id.songTitle);
        mArtistTitle = findViewById(R.id.artistTitle);
        mAlbumTitle = findViewById(R.id.albumTitle);

    }

    private void loadFragments() {
        mFragments.add(new SongsListFragment());
        mFragments.add(new MusicControllerFragment());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPlayerService.setCallback(null);
        unbindService(mServiceConnection);
    }

    public void setUriInService(String uri) {

        mPlayerService.setUri(uri);
    }

    public void setCurrentSongInService (Song song) {

        if (mBound) mPlayerService.setCurrentSong(song);
    }

    public void playMusic(boolean prepare) {

        if(mBound) {

            Log.d(TAG, "playMusic: mBound = " + mBound + " \nPlaying Music...");
            mPlayerService.playMusic(prepare);

        }

    }

    private Handler mHandler = new Handler();

    @SuppressLint("DefaultLocale")
    public void setSeekBarProgress() {

        // Set Max Time Label

        mMaxLabel = findViewById(R.id.maxTimeTextView);
        mElapsedLabel = findViewById(R.id.elapsedTimeTextView);
        mSeekBar = findViewById(R.id.seekBar);

        // Update Elapsed Time within the runnable

        if(mBound) {

            int duration = mPlayerService.mMediaPlayer.getDuration();

            String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );

            mMaxLabel.setText(time);

            Log.d(TAG, "setSeekBarProgress: 1");
            if (mPlayerService.mMediaPlayer != null) {

                Log.d(TAG, "setSeekBarProgress: 2");
//                if (mPlayerService.mMediaPlayer.isPlaying()) {


                    int maxProgress = mPlayerService.mMediaPlayer.getDuration() / 1000;
                    mSeekBar.setMax(maxProgress);
                    Log.d(TAG, "run: MaxProgress: " + maxProgress);

                    Log.d(TAG, "setSeekBarProgress: 4");
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            int currentProgress = mPlayerService.mMediaPlayer.getCurrentPosition() / 1000;
                            mSeekBar.setProgress(currentProgress);
                            Log.d(TAG, "run: CurrentProgress: " + currentProgress);

                            int elapsed = mPlayerService.mMediaPlayer.getCurrentPosition();

                            String time = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(elapsed),
                                    TimeUnit.MILLISECONDS.toSeconds(elapsed) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed))
                            );

                            mElapsedLabel.setText(time);


                            mHandler.postDelayed(this, 1000);
                        }
                    };

                    MainActivity.this.runOnUiThread(runnable);

//                }
            }
        }

    }

    public void setMediaPlayerProgress(int progress) {

        if (mBound) {

            mPlayerService.mMediaPlayer.seekTo(progress * 1000);
        }
    }

    @Override
    public void doSomething() {

        Toast.makeText(this, "I'm here!", Toast.LENGTH_SHORT).show();
        setSeekBarProgress();
        mViewPager.setCurrentItem(1, true);

    }

    @Override
    public void updateViewsInControllerFragment(Song song) {

//        mSongTitle.setText(song.getSongName());
//        mAlbumArtImageView.setImageBitmap(song.getAlbumArt());


    }

    public void setCallBackInService(PlayerService.ServiceCallbacks callback) {

        if (mBound) {
            mPlayerService.setCallback(callback);
        }
    }
}
