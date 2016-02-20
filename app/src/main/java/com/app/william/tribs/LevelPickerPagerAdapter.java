package com.app.william.tribs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by William on 2/19/2016.
 */
public class LevelPickerPagerAdapter extends FragmentPagerAdapter {
    private int mLevels;


    public LevelPickerPagerAdapter(FragmentManager fm, int levels){
        super(fm);

        mLevels = levels;
    }

    @Override
    public Fragment getItem(int position) {
        return LevelPickerFragment.newInstance(position * 16 + 1, mLevels < position * 16 + 16 ? mLevels : position * 16 + 16);
    }

    @Override
    public int getCount() {
        return mLevels / 16 + ((mLevels % 16 == 0) ? 0 : 1);
    }
}
