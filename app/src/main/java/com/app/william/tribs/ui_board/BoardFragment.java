package com.app.william.tribs.ui_board;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.app.william.tribs.Model;
import com.app.william.tribs.R;

import java.util.List;

/**
 * Created by William on 2/15/2016.
 */
public class BoardFragment extends Fragment implements BoardGridAdapter.BoardButtonPressed {

    private LinearLayout answersRowTwo;
    private BoardLayout mBoardLayout;
    private Model mModel;
    private int mLevel;
    private boolean levelStarted = false;
    private GridView mBoardGrid;
    private GridView mAnswerGrid;
    private BoardGridAdapter mGridAdapter;
    private BoardGridAdapter mAnswersAdapter;

    public static BoardFragment newInstance(int level) {
        BoardFragment myFragment = new BoardFragment();

        Bundle args = new Bundle();
        args.putInt("level", level);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLevel = getArguments().getInt("level");
        mModel = new Model(this, getContext(), (TribsDragListener.ChangeLevelCallBacks) getContext());

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_fragment_layout, container, false);

        mBoardGrid = (GridView) view.findViewById(R.id.board_grid);

        mAnswerGrid = (GridView) view.findViewById(R.id.answers_grid);

        mBoardLayout = (BoardLayout) view.findViewById(R.id.board);

        mModel.startlevel(mLevel);

        return view;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event, int position) {
        if(!view.isClickable()) return true;

        int y = (int) Math.floor(position/5);
        int x = position % 5;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mModel.blockDown(x,y);
                break;
            case MotionEvent.ACTION_UP:
                mModel.blockSelected(x, y);
                break;
        }
        return true;
    }

    public void setGrid(int w, int h, int val) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setText(String.valueOf(val));
        ((Button) mGridAdapter.getItem(w + 5 * h)).setClickable(true);
        unSetButtonSelected(w, h);
    }

    public void setAnswer(int i, int val) {
        ((Button) mAnswersAdapter.getItem(i)).setText(String.valueOf(val));
        ((Button) mAnswersAdapter.getItem(i)).setClickable(false);
        setUnAnswered(i);
    }

    public void setButtonSelected(int w, int h) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_green));
    }

    public void downBlock(int w, int h) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_white_pressed));
    }

    public void unSetButtonSelected(int w, int h) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blank_with_downstate));
    }

    public void setButtonAnswered(int w, int h) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blue));
        ((Button) mGridAdapter.getItem(w + 5 * h)).setClickable(false);
    }

    public void setAnswered(int count) {
        ((Button) mAnswersAdapter.getItem(count)).setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_gold));
    }

    public void setUnAnswered(int count) {
        ((Button) mAnswersAdapter.getItem(count)).setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_white));
    }

    public void setAnswerUnVisible(int count) {
        ((Button) mAnswersAdapter.getItem(count)).setVisibility(View.GONE);
        if (count > 3) {
            answersRowTwo.invalidate();
        }
    }

    public void setAnswerVisible(int count) {
        ((Button) mAnswersAdapter.getItem(count)).setVisibility(View.VISIBLE);
        if (count > 3) {
            answersRowTwo.invalidate();
        }
    }


    public void setWrong(final int w, final int h) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_red_pressed));
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unSetButtonSelected(w, h);
            }
        }, 200);

    }

    public void setHorsSelected(int h1, int w1, int h2, int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#66CD00"));
    }

    public void setHorsUnSelected(int h1, int w1, int h2, int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#EEEEEE"));
    }

    public void setHorsAnswered(int h1, int w1, int h2, int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.BLUE);
    }

    public void setHorsWrong(final int h1, final int w1, final int h2, final int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.RED);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setHorsUnSelected(h1, w1, h2, w2);
            }
        }, 200);
    }

    public void setVerSelected(int h1, int w1, int h2, int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#66CD00"));
    }

    public void setVerUnSelected(int h1, int w1, int h2, int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#EEEEEE"));
    }

    public void setVerAnswered(int h1, int w1, int h2, int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.BLUE);
    }

    public void setVerWrong(final int h1, final int w1, final int h2, final int w2) {
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.RED);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setVerUnSelected(h1, w1, h2, w2);
            }
        }, 200);
    }

    public void setmModel(Model mModel) {
        this.mModel = mModel;
    }

    public void setBlock(int w, int h, int val) {
        ((Button) mGridAdapter.getItem(w + 5 * h)).setClickable(false);
        ((Button) mGridAdapter.getItem(w + 5 * h)).setBackgroundDrawable(getResources().getDrawable(R.mipmap.tile_block));
    }

    public void setGrid(List<Integer> blocks) {
        mGridAdapter = new BoardGridAdapter(getContext(), blocks, this, 25, false);
        mBoardGrid.setAdapter(mGridAdapter);
    }

    public void setAnswers(List<Integer> answers) {
        mAnswersAdapter = new BoardGridAdapter(getContext(), answers, this, answers.size(), true);
        mAnswerGrid.setAdapter(mAnswersAdapter);
    }

    public void repeat(){
        mModel.startlevel(mLevel);
    }

    public void clearLines() {
        mBoardLayout.clear();
    }

    public void startTutorial(){
        if(mLevel == 0){
            new TribsTutorial(getContext(), mModel, mGridAdapter, mAnswersAdapter);
        }
    }
}
