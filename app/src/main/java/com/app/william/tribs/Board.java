package com.app.william.tribs;

import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Board extends ActionBarActivity implements LevelPickerFragment.StartLevelCallBack{

    private static final int[] BOARD_IDS={
            R.id.s1_1,
            R.id.s1_2,
            R.id.s1_3,
            R.id.s1_4,
            R.id.s1_5,
            R.id.s2_1,
            R.id.s2_2,
            R.id.s2_3,
            R.id.s2_4,
            R.id.s2_5,
            R.id.s3_1,
            R.id.s3_2,
            R.id.s3_3,
            R.id.s3_4,
            R.id.s3_5,
            R.id.s4_1,
            R.id.s4_2,
            R.id.s4_3,
            R.id.s4_4,
            R.id.s4_5,
            R.id.s5_1,
            R.id.s5_2,
            R.id.s5_3,
            R.id.s5_4,
            R.id.s5_5
    };
    private static final int[] ANSWER_IDS={
            R.id.a1_1,
            R.id.a1_2,
            R.id.a1_3,
            R.id.a1_4,
            R.id.a2_1,
            R.id.a2_2,
            R.id.a2_3,
            R.id.a2_4
    };
    private static final int[] CONNECTOR_IDS_HORS={
            R.id.c11_12,
            R.id.c12_13,
            R.id.c13_14,
            R.id.c14_15,
            R.id.c21_22,
            R.id.c22_23,
            R.id.c23_24,
            R.id.c24_25,
            R.id.c31_32,
            R.id.c32_33,
            R.id.c33_34,
            R.id.c34_35,
            R.id.c41_42,
            R.id.c42_43,
            R.id.c43_44,
            R.id.c44_45,
            R.id.c51_52,
            R.id.c52_53,
            R.id.c53_54,
            R.id.c54_55
    };
    private static final int[] CONNECTOR_IDS_VER={
            R.id.c11_21,
            R.id.c12_22,
            R.id.c13_23,
            R.id.c14_24,
            R.id.c15_25,
            R.id.c21_31,
            R.id.c22_32,
            R.id.c23_33,
            R.id.c24_34,
            R.id.c25_35,
            R.id.c31_41,
            R.id.c32_42,
            R.id.c33_43,
            R.id.c34_44,
            R.id.c35_45,
            R.id.c41_51,
            R.id.c42_52,
            R.id.c43_53,
            R.id.c44_54,
            R.id.c45_55,
    };

    private Model model;
    private TextView levelLbl;
    private static String TRIBS_PREFS = "Tribs_Prefs";
    private LinearLayout picker_menu;
    private int picker_width;
    private RelativeLayout picker_action_bar;
    private ViewPager mViewPager;
    private BoardPagerAdapter mBoardPagerAdapter;
    private ViewPager mPickerPager;
    TribsDragListener mDragListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        model = new Model(this, this);

        picker_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());


        getSupportActionBar().hide();

        picker_action_bar = (RelativeLayout) findViewById(R.id.actionbar_picker);

        TextView quit = (TextView) findViewById(R.id.quit);
        Button nextLvl = (Button) findViewById(R.id.nextLvl);
        Button preLvl = (Button) findViewById(R.id.prevLvl);
        Button refresh = (Button) findViewById(R.id.refresh);

        levelLbl = (TextView) findViewById(R.id.lvlTitle);


        picker_menu = (LinearLayout) findViewById(R.id.level_picker_menu);
        picker_menu.setY(-picker_width);

        final ViewConfiguration vc = ViewConfiguration.get(this);
        final int swipeMinDistance = vc.getScaledPagingTouchSlop();

        mDragListener = new TribsDragListener(picker_action_bar, picker_menu, picker_width, swipeMinDistance/2, model);

        levelLbl.setOnTouchListener(mDragListener);

        quit.setOnTouchListener(mDragListener);

        nextLvl.setOnTouchListener(mDragListener);

        preLvl.setOnTouchListener(mDragListener);

        refresh.setOnTouchListener(mDragListener);
        picker_action_bar.setOnTouchListener(mDragListener);
        picker_menu.setOnTouchListener(mDragListener);


        mViewPager = (ViewPager) findViewById(R.id.board_pager);

        mBoardPagerAdapter = new BoardPagerAdapter(model, getSupportFragmentManager(), model.getMAX_LEVEL());

        mViewPager.setAdapter(mBoardPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BoardFragment frag = mBoardPagerAdapter.getPrimary();

                if (frag != null && positionOffset == 0 && positionOffsetPixels == 0) {
                    model.startlevel(position, frag);
                    frag.setmModel(model);
                }
            }

            @Override
            public void onPageSelected(int position) {
                BoardFragment frag = mBoardPagerAdapter.getPrimary();

                if (frag != null) {
                    model.startlevel(position, frag);
                    frag.setmModel(model);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPickerPager = (ViewPager) findViewById(R.id.level_picker_pager);
        LevelPickerPagerAdapter levelPickerPagerAdapter = new LevelPickerPagerAdapter(getSupportFragmentManager(), model.getMAX_LEVEL());
        mPickerPager.setAdapter(levelPickerPagerAdapter);

        SharedPreferences preferences = getSharedPreferences(TRIBS_PREFS, 0);
        if(preferences.getBoolean("first_time", true)) {
            preferences.edit().putBoolean("first_time", false).commit();
            model.startTutorial();
        }else {
            mViewPager.setCurrentItem(1);

        }
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

    public void setTitle(int level){
        if(level ==0) {
            levelLbl.setText("Tutorial");
        }else{
            levelLbl.setText("Level " + level);
        }
    }

    public void setLevel(int level){
        mViewPager.setCurrentItem(level);
    }

    @Override
    public void startLevel(int i) {
        mViewPager.setCurrentItem(i);
        mDragListener.closePicker(20);
    }
}
