package com.arpan.musicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter{
    private final ArrayList<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);

        mFragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}