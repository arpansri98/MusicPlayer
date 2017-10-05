package com.arpan.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ActivityHandler extends Handler {
    private MainActivity mMainActivity;

    public ActivityHandler(MainActivity mainActivity) {
        mMainActivity = mainActivity;

    }

    @Override
    public void handleMessage(Message msg) {

        Bundle msgData = new Bundle();
        msgData.putString("IS_PLAYING", "TRUE");

        msg.setData(msgData);

    }
}
