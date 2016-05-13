package com.app.william.tribs.ui_board;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 3/5/2016.
 */
public class BoardLayout extends LinearLayout {
    private List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> mConnectors;
    private List<Integer> mColours;
    private Paint mPaint;
    private int mWidth;
    private int mLength;

    public BoardLayout(Context context) {
        super(context);

        init();
    }

    public BoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BoardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init(){
        mConnectors = new ArrayList<>();
        mColours = new ArrayList<>();

        mPaint = new Paint();

        mPaint.setStrokeWidth(40);


    }

    public void addNewConnection(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> newConnection, int colour){
        for(int i = 0; i < mConnectors.size(); i++){
            if(mConnectors.get(i) == newConnection){
                mColours.set(i, colour);
                requestLayout();
                return;
            }
        }

        mConnectors.add(newConnection);
        mColours.add(colour);

        requestLayout();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int widthInterval = mWidth/10;
        int lengthInterval = mLength/10;
        int i = 0;

        for(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> xySet : mConnectors){
            mPaint.setColor(mColours.get(i));
            canvas.drawLine(xySet.first.first * widthInterval * 2 + widthInterval + getPaddingRight(), xySet.first.second * lengthInterval * 2 + lengthInterval + getPaddingTop(),
                    xySet.second.first * widthInterval * 2 + widthInterval + getPaddingRight(), xySet.second.second * lengthInterval * 2 + lengthInterval + getPaddingTop(), mPaint);

            i++;
        }

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w - getPaddingLeft() - getPaddingRight();
        mLength = h - getPaddingBottom() - getPaddingTop();


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mWidth = (r-l) - getPaddingLeft() - getPaddingRight();
        mLength = (b-t) - getPaddingBottom() - getPaddingTop();

        if(mWidth != mLength) {
            if (mWidth < mLength) {
                setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mWidth));
            } else {
                setLayoutParams(new RelativeLayout.LayoutParams(mLength, mLength));
            }
            requestLayout();
        }
    }

    public void clear(){
        mConnectors = new ArrayList<>();
        mColours = new ArrayList<>();

        requestLayout();
    }
}
