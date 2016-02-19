package com.app.william.tribs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by William on 2/15/2016.
 */
public class BoardPagerAdapter extends FragmentPagerAdapter {
    private Model mModel;
    private FragmentManager mFm;
    int mNumLevels;
    private BoardFragment mPrimaryFrag;



    public BoardPagerAdapter(Model model, FragmentManager fm, int numLevels){
        super(fm);

        mModel = model;
        mFm = fm;
        mNumLevels = numLevels;
    }

    @Override
    public Fragment getItem(int position) {
        BoardFragment frag = BoardFragment.newInstance(position);

        return frag;
    }

    @Override
    public int getCount() {
        return mNumLevels;
    }

    public BoardFragment getPrimary(){
        return mPrimaryFrag;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        mPrimaryFrag = (BoardFragment) object;
    }
}
