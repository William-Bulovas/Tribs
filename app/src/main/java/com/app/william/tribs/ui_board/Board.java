package com.app.william.tribs.ui_board;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.app.william.tribs.R;
import com.app.william.tribs.databinding.ActivityBoardBinding;
import com.app.william.tribs.ui_level_picker.LevelPickerFragment;
import com.app.william.tribs.ui_level_picker.LevelPickerPagerAdapter;


public class Board extends AppCompatActivity implements LevelPickerFragment.StartLevelCallBack, StartScreenFragment.StartScreenCallBacks, TribsDragListener.ChangeLevelCallBacks{
    private static int MAXLEVELS = 17;

    private static String TRIBS_PREFS = "Tribs_Prefs";
    private int picker_width;
    private BoardPagerAdapter mBoardPagerAdapter;
    TribsDragListener mDragListener;
    private int mFarthestLevel;
    private LevelPickerPagerAdapter mLevelPickerPagerAdapter;
    private ActivityBoardBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_board);

        picker_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

        getSupportActionBar().hide();

        mBinding.levelPickerMenu.setY(-picker_width);

        final ViewConfiguration vc = ViewConfiguration.get(this);
        final int swipeMinDistance = vc.getScaledPagingTouchSlop();

        mDragListener = new TribsDragListener(mBinding.actionbarPicker, mBinding.levelPickerMenu, picker_width, swipeMinDistance / 2, this);

        mBinding.lvlTitle.setOnTouchListener(mDragListener);
        mBinding.quit.setOnTouchListener(mDragListener);
        mBinding.nextLvl.setOnTouchListener(mDragListener);
        mBinding.prevLvl.setOnTouchListener(mDragListener);
        mBinding.refresh.setOnTouchListener(mDragListener);
        mBinding.actionbarPicker.setOnTouchListener(mDragListener);
        mBinding.levelPickerMenu.setOnTouchListener(mDragListener);

        mBoardPagerAdapter = new BoardPagerAdapter(getSupportFragmentManager(), MAXLEVELS);

        mBinding.boardPager.setAdapter(mBoardPagerAdapter);

        mBinding.boardPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BoardFragment frag = mBoardPagerAdapter.getPrimary();
                setTitle(position);

                if (frag != null && positionOffset == 0 && positionOffsetPixels == 0) {

                    if (position == mFarthestLevel + 1) {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp);
                        //fadedArrow.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.MULTIPLY);
                        mBinding.nextLvl.setBackgroundDrawable(fadedArrow);
                        mBinding.nextLvl.setActivated(false);
                    } else {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp);
                        mBinding.nextLvl.setBackgroundDrawable(fadedArrow);
                        mBinding.nextLvl.setClickable(true);
                    }

                    if (position == 0 || position == 1) {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp);
                        //fadedArrow.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.MULTIPLY);
                        mBinding.nextLvl.setBackgroundDrawable(fadedArrow);
                        mBinding.nextLvl.setClickable(false);
                    } else {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp);
                        mBinding.prevLvl.setBackgroundDrawable(fadedArrow);
                        mBinding.prevLvl.setClickable(true);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SharedPreferences preferences = getSharedPreferences(TRIBS_PREFS, 0);
        if (preferences.getBoolean("first_time", true)) {
            preferences.edit().putBoolean("first_time", false).apply();
            mFarthestLevel = -1;
        } else {
            mFarthestLevel = preferences.getInt("farthest_level", 0);
        }

        mLevelPickerPagerAdapter = new LevelPickerPagerAdapter(getSupportFragmentManager(), MAXLEVELS, mFarthestLevel);
        mBinding.levelPickerPager.setAdapter(mLevelPickerPagerAdapter);

        mBinding.boardPager.setCurrentItem(mFarthestLevel + 1);

        showStartScreen();
    }

    @Override
    protected void onPause() {
        SharedPreferences preferences = getSharedPreferences(TRIBS_PREFS, 0);
        preferences.edit().putInt("farthest_level", mFarthestLevel).apply();

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitle(int level) {
        if (level == 0) {
            mBinding.setTitle("Tutorial");
        } else {
            mBinding.setTitle("Level " + level);
        }
    }

    public void setLevel(int level) {
        mBinding.boardPager.setCurrentItem(level);
    }

    @Override
    public void startLevel(int i) {
        mBinding.boardPager.setCurrentItem(i);
        mDragListener.closePicker(20);
    }

    public void setFarthest(int i) {
        if (mFarthestLevel < i) {
            mFarthestLevel = i;
            mLevelPickerPagerAdapter.setFarthest(i);
        }
    }

    private void showStartScreen() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.board_root, new StartScreenFragment())
                .addToBackStack("StartScreen")
                .commit();
    }

    @Override
    public void startGame() {
        getSupportFragmentManager().popBackStack();

        mBoardPagerAdapter.getPrimary().startTutorial();
    }


    @Override
    public void increaseLevel() {
        if(mBinding.boardPager.getCurrentItem() + 1 <= MAXLEVELS && mBinding.boardPager.getCurrentItem() + 1 <= mFarthestLevel + 1){
            setLevel(mBinding.boardPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void quit() {
        onBackPressed();
    }

    @Override
    public void repeatLevel() {
        mBoardPagerAdapter.getPrimary().repeat();
    }

    @Override
    public void decreaseLevel() {
        if(mBinding.boardPager.getCurrentItem() - 1 >= 1){
            setLevel(mBinding.boardPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void beatCurrentLevel() {
        setFarthest(mBinding.boardPager.getCurrentItem());
    }
}
