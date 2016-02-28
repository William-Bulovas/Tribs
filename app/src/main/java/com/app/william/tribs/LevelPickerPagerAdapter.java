package com.app.william.tribs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by William on 2/19/2016.
 */
public class LevelPickerPagerAdapter extends FragmentPagerAdapter {
    private int mLevels;
    private int mFarthest;
    private LevelPickerFragment mPrimaryFrag;

    public LevelPickerPagerAdapter(FragmentManager fm, int levels, int farthest){
        super(fm);

        mLevels = levels;
        mFarthest = farthest;
    }

    @Override
    public Fragment getItem(int position) {
        return LevelPickerFragment.newInstance(position * 16 + 1, mLevels < position * 16 + 16 ? mLevels : position * 16 + 16, mFarthest);
    }

    @Override
    public int getCount() {
        return mLevels / 16 + ((mLevels % 16 == 0) ? 0 : 1);
    }

    public void setFarthest(int farthest){
        mFarthest = farthest;
        mPrimaryFrag.setFarthest(farthest);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        mPrimaryFrag = (LevelPickerFragment) object;
    }
}
