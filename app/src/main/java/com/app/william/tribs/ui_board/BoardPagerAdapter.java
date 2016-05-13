package com.app.william.tribs.ui_board;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.app.william.tribs.Model;

/**
 * Created by William on 2/15/2016.
 */
public class BoardPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFm;
    int mNumLevels;
    private BoardFragment mPrimaryFrag;



    public BoardPagerAdapter(FragmentManager fm, int numLevels){
        super(fm);

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
        return mNumLevels + 1;
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
