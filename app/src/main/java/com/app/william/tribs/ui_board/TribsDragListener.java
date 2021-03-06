package com.app.william.tribs.ui_board;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.william.tribs.Model;
import com.app.william.tribs.R;

/**
 * Created by William on 2/11/2016.
 */
public class TribsDragListener implements View.OnTouchListener, View.OnClickListener{
    float dY, downY;
    private LinearLayout mPickerMenu;
    private RelativeLayout mPickerActionBar;
    private boolean picker_action_mode;
    private int mPickerWidth;
    private int mThreshold;
    private boolean pastThreshold = false;
    private ChangeLevelCallBacks mCallBacks;

    public TribsDragListener(RelativeLayout picker_action_bar, LinearLayout picker_menu, int picker_width, int threshold, ChangeLevelCallBacks callBacks){
        mPickerActionBar = picker_action_bar;
        mPickerMenu = picker_menu;
        mPickerWidth = picker_width;
        mThreshold = threshold;
        mCallBacks = callBacks;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float newy = event.getRawY() + dY;
        if(v == mPickerMenu){
            newy += mPickerWidth;
        }
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                dY = v.getY() - event.getRawY();
                downY = event.getRawY();
                pastThreshold = false;
                break;

            case MotionEvent.ACTION_MOVE:

                float uY = event.getRawY();

                float delY = downY - uY > 0 ? downY - uY : uY - downY;

                if(Math.abs(delY) < mThreshold && !pastThreshold){

                    return true;
                }
                pastThreshold = true;
                if(newy < mPickerWidth + mThreshold + 10 && newy > 0) {
                    mPickerActionBar.animate()
                            .y(newy)
                            .setDuration(0)
                            .start();
                    mPickerMenu.animate()
                            .y(newy - mPickerWidth)
                            .setDuration(0)
                            .start();
                }
                break;
            case MotionEvent.ACTION_UP:
                float upY = event.getRawY();

                float deltaY = downY - upY > 0 ? downY - upY : upY - downY;

                if(Math.abs(deltaY) < mThreshold && !pastThreshold){
                    onClick(v);

                    return true;
                }
                if(event.getRawY() > downY) {
                    picker_action_mode = true;
                    if (newy < mPickerWidth) {
                        openPicker(mPickerWidth - newy);
                    } else{
                        openPicker(newy - mPickerWidth);
                    }
                } else if(newy > 0){
                    picker_action_mode = false;
                    closePicker(newy);
                } else{
                    picker_action_mode = false;
                    closePicker(20);
                }
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lvlTitle:
                if (!picker_action_mode) {
                    picker_action_mode = true;
                    openPicker(mPickerWidth);
                } else {
                    picker_action_mode = false;
                    closePicker(mPickerWidth);
                }
                break;
            case R.id.prevLvl:
                mCallBacks.decreaseLevel();
                break;
            case R.id.nextLvl:
                mCallBacks.increaseLevel();
                break;
            case R.id.quit:
                mCallBacks.quit();
                break;
            case R.id.refresh:
                mCallBacks.repeatLevel();
                break;
        }
    }

    public void openPicker(float animationTime){
        mPickerActionBar.animate()
                .y(mPickerWidth)
                .setDuration((long) animationTime / 5)
                .start();
        mPickerMenu.animate()
                .y(0)
                .setDuration((long) animationTime / 5)
                .start();
    }

    public void closePicker(float animationTime){
        mPickerActionBar.animate()
                .y(0)
                .setDuration((long) (animationTime) / 5)
                .start();
        mPickerMenu.animate()
                .y(-mPickerWidth)
                .setDuration((long) (animationTime) / 5)
                .start();
    }

    public interface ChangeLevelCallBacks{
        public void increaseLevel();

        public void quit();

        public void repeatLevel();

        public void decreaseLevel();

        public void beatCurrentLevel();
    }
}
