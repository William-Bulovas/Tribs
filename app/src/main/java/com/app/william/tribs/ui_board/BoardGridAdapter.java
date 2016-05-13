package com.app.william.tribs.ui_board;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.app.william.tribs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 5/9/2016.
 */
public class BoardGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Integer> mBoardNumbers;
    private int mPadding;
    private List<Button> mButtons;
    private BoardButtonPressed mButtonCallbacks;
    private int mNum;
    private boolean mAnswers = false;

    public BoardGridAdapter(Context context, List<Integer> boardNumbers, BoardButtonPressed boardButtonPressed, int num, boolean answers) {
        super();

        mContext = context;
        mBoardNumbers = boardNumbers;
        mPadding = (int) (10 * mContext.getResources().getSystem().getDisplayMetrics().density);
        mButtons = new ArrayList<>();
        mButtonCallbacks = boardButtonPressed;
        mNum = num;
        mAnswers = answers;

        for (int position = 0; position < mNum; position++) {
            mButtons.add(position, new Button(mContext));
            if (mBoardNumbers.get(position) > 0) {
                mButtons.get(position).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tile_white));
            } else {
                mButtons.get(position).setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.tile_block));
                mButtons.get(position).setClickable(false);
            }
        }
    }

    @Override
    public int getCount() {
        return mNum;
    }

    @Override
    public Object getItem(int position) {
        return mButtons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        float width = parent.getWidth() - mPadding * (mAnswers ? 3 : 4);
        if (convertView == null) {
            if(mBoardNumbers.get(position)> 0) mButtons.get(position).setText(String.valueOf(mBoardNumbers.get(position)));
            if (!mAnswers) {
                mButtons.get(position).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mButtonCallbacks.onTouch(v, event, position);
                    }
                });
            }
            mButtons.get(position).requestLayout();
        }
        mButtons.get(position).setLayoutParams(new GridView.LayoutParams(Math.round(width / (mAnswers ? 4 : 5)), Math.round(width / (mAnswers ? 4 : 5))));

        return mButtons.get(position);
    }

    public interface BoardButtonPressed {
        public boolean onTouch(View view, MotionEvent motionEvent, int position);
    }
}
