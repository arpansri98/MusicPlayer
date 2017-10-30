package com.arpan.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arpan.musicplayer.model.Song;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class PlayerService extends Service{

    public IBinder mBinder = new LocalBinder();

    public MediaPlayer mMediaPlayer;

    private String  mUri;

    private Song mCurrentSong;
    private int elapsedTime;

    public void resumeMusic() {

        mMediaPlayer.start();
        mMediaPlayer.seekTo(elapsedTime / 1000);
    }

    public interface ServiceCallbacks {

        void updateViewsInControllerFragment(Song song);
    }

    private  ServiceCallbacks serviceCallbacks;

    public void setCallback(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    @Override
    public void onCreate() {

        mMediaPlayer = new MediaPlayer();


    }

    @Override
    public void onDestroy() {

        mMediaPlayer.release();
        Log.d(TAG, "onDestroy: Service Destroyed");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;

    }

    public class LocalBinder extends Binder {

        public PlayerService getService() {

            return PlayerService.this;

        }

    }

    //Client Methods
    public void setUri(String uri) {

        mUri = uri;

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCurrentSong(Song currentSong) {

        mCurrentSong = currentSong;
    }

    public Song getCurrentSong() {
        return mCurrentSong;
    }

    public void playMusic(boolean prepare) {

        Log.d(TAG, "playMusic: PlayMusic Invoked.");

        if(prepare) mMediaPlayer.prepareAsync();

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();

                if (serviceCallbacks != null) {
                    serviceCallbacks.updateViewsInControllerFragment(mCurrentSong);
                }

            }
        });

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

            }
        });



    }

    public void pauseMusic() {
        elapsedTime = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.pause();
        Log.d(TAG, "pauseMusic: ElapsedTime: " + elapsedTime);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public MediaPlayer getMediaPlayer() { return mMediaPlayer; }
}
