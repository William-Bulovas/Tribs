package com.app.william.tribs.ui_board;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.william.tribs.ui_level_picker.LevelPickerFragment;
import com.app.william.tribs.ui_level_picker.LevelPickerPagerAdapter;
import com.app.william.tribs.Model;
import com.app.william.tribs.R;


public class Board extends ActionBarActivity implements LevelPickerFragment.StartLevelCallBack, StartScreenFragment.StartScreenCallBacks, TribsDragListener.ChangeLevelCallBacks{
    private static int MAXLEVELS = 17;

    private TextView levelLbl;
    private static String TRIBS_PREFS = "Tribs_Prefs";
    private LinearLayout picker_menu;
    private int picker_width;
    private RelativeLayout picker_action_bar;
    private ViewPager mViewPager;
    private BoardPagerAdapter mBoardPagerAdapter;
    private ViewPager mPickerPager;
    TribsDragListener mDragListener;
    private int mFarthestLevel;
    private Button mNextLvl;
    private Button mPrevLvl;
    private LevelPickerPagerAdapter mLevelPickerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        picker_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());


        getSupportActionBar().hide();

        picker_action_bar = (RelativeLayout) findViewById(R.id.actionbar_picker);

        TextView quit = (TextView) findViewById(R.id.quit);
        mNextLvl = (Button) findViewById(R.id.nextLvl);
        mPrevLvl = (Button) findViewById(R.id.prevLvl);
        Button refresh = (Button) findViewById(R.id.refresh);

        levelLbl = (TextView) findViewById(R.id.lvlTitle);


        picker_menu = (LinearLayout) findViewById(R.id.level_picker_menu);
        picker_menu.setY(-picker_width);

        final ViewConfiguration vc = ViewConfiguration.get(this);
        final int swipeMinDistance = vc.getScaledPagingTouchSlop();

        mDragListener = new TribsDragListener(picker_action_bar, picker_menu, picker_width, swipeMinDistance / 2, this);

        levelLbl.setOnTouchListener(mDragListener);

        quit.setOnTouchListener(mDragListener);

        mNextLvl.setOnTouchListener(mDragListener);

        mPrevLvl.setOnTouchListener(mDragListener);

        refresh.setOnTouchListener(mDragListener);
        picker_action_bar.setOnTouchListener(mDragListener);
        picker_menu.setOnTouchListener(mDragListener);


        mViewPager = (ViewPager) findViewById(R.id.board_pager);

        mBoardPagerAdapter = new BoardPagerAdapter(getSupportFragmentManager(), MAXLEVELS);

        mViewPager.setAdapter(mBoardPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BoardFragment frag = mBoardPagerAdapter.getPrimary();
                setTitle(position);

                if (frag != null && positionOffset == 0 && positionOffsetPixels == 0) {

                    if (position == mFarthestLevel + 1) {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp);
                        //fadedArrow.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.MULTIPLY);
                        mNextLvl.setBackgroundDrawable(fadedArrow);
                        mNextLvl.setActivated(false);
                    } else {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp);
                        mNextLvl.setBackgroundDrawable(fadedArrow);
                        mNextLvl.setClickable(true);
                    }

                    if (position == 0 || position == 1) {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp);
                        //fadedArrow.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.MULTIPLY);
                        mPrevLvl.setBackgroundDrawable(fadedArrow);
                        mPrevLvl.setClickable(false);
                    } else {
                        Drawable fadedArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_24dp);
                        mPrevLvl.setBackgroundDrawable(fadedArrow);
                        mPrevLvl.setClickable(true);
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

        mPickerPager = (ViewPager) findViewById(R.id.level_picker_pager);
        mLevelPickerPagerAdapter = new LevelPickerPagerAdapter(getSupportFragmentManager(), MAXLEVELS, mFarthestLevel);
        mPickerPager.setAdapter(mLevelPickerPagerAdapter);

        mViewPager.setCurrentItem(mFarthestLevel + 1);

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
            levelLbl.setText("Tutorial");
        } else {
            levelLbl.setText("Level " + level);
        }
    }

    public void setLevel(int level) {
        mViewPager.setCurrentItem(level);
    }

    @Override
    public void startLevel(int i) {
        mViewPager.setCurrentItem(i);
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
        if(mViewPager.getCurrentItem() + 1 <= MAXLEVELS && mViewPager.getCurrentItem() + 1 <= mFarthestLevel + 1){
            setLevel(mViewPager.getCurrentItem() + 1);
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
        if(mViewPager.getCurrentItem() - 1 >= 1){
            setLevel(mViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void beatCurrentLevel() {
        setFarthest(mViewPager.getCurrentItem());
    }
}
