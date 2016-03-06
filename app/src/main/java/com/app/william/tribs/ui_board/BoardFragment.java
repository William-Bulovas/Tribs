package com.app.william.tribs.ui_board;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.william.tribs.Model;
import com.app.william.tribs.R;

/**
 * Created by William on 2/15/2016.
 */
public class BoardFragment extends Fragment implements View.OnClickListener{
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

    private Button grid[] = new Button[25];
    private Button answers[] = new Button[8];
    private LinearLayout answersRowTwo;
    private BoardLayout mBoardLayout;
    private Model mModel;
    private int mLevel;
    private boolean levelStarted = false;

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

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_fragment_layout, container, false);
        grid = new Button[25];
        answers = new Button[8];

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                grid[i+ 5 *j] = (Button) view.findViewById(BOARD_IDS[i + 5*j]);
                grid[i + 5 * j].setOnClickListener(this);
                grid[i+ 5 * j].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blank_with_downstate));
            }
        }

        for(int i =0 ; i < 4; i++){
            for(int j = 0; j < 2; j++){
                answers[i+ 4 *j] = (Button) view.findViewById(ANSWER_IDS[i + 4*j]);
                answers[i+ 4 * j].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_white));
            }
        }

        mBoardLayout = (BoardLayout) view.findViewById(R.id.board);

        answersRowTwo = (LinearLayout) view.findViewById(R.id.answers_row_two);

        return view;
    }


    public void startLevel(){
        mModel.startlevel(mLevel, this);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(BOARD_IDS[i + j *5] == id) {
                    mModel.blockSelected(i, j);
                    break;
                }
            }
        }
    }



    public void setGrid(int w, int h, int val){
        grid[w+ 5 * h].setText(String.valueOf(val));
        grid[w + 5 * h].setClickable(true);
        unSetButtonSelected(w, h);
    }

    public void setAnswer(int i, int val){
        answers[i].setText(String.valueOf(val));
        answers[i].setClickable(false);
        setUnAnswered(i);
    }

    public void setButtonSelected(int w, int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_green));
    }

    public void unSetButtonSelected(int w, int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blank_with_downstate));
    }

    public void setButtonAnswered(int w, int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blue));
        grid[w + 5 * h].setClickable(false);
    }

    public void setAnswered(int count){
        answers[count].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_gold));
    }

    public void setUnAnswered(int count){
        answers[count].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_white));
    }

    public void setAnswerUnVisible(int count){
        answers[count].setVisibility(View.GONE);
        if(count > 3){
            answersRowTwo.invalidate();
        }
    }

    public void setAnswerVisible(int count){
        answers[count].setVisibility(View.VISIBLE);
        if(count > 3){
            answersRowTwo.invalidate();
        }
    }



    public void setWrong(final int w, final int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_red_pressed));
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unSetButtonSelected(w, h);
            }
        }, 100);

    }

    public void setHorsSelected(int h1, int w1, int h2, int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#66CD00"));
    }
    public void setHorsUnSelected(int h1, int w1, int h2, int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#EEEEEE"));
    }

    public void setHorsAnswered(int h1, int w1, int h2, int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.BLUE);
    }

    public void setHorsWrong(final int h1, final int w1, final int h2, final int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.RED);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setHorsUnSelected(h1, w1, h2, w2);
            }
        }, 100);
    }

    public void setVerSelected(int h1, int w1, int h2, int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#66CD00"));
    }
    public void setVerUnSelected(int h1, int w1, int h2, int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.parseColor("#EEEEEE"));
    }

    public void setVerAnswered(int h1, int w1, int h2, int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.BLUE);
    }

    public void setVerWrong(final int h1, final int w1, final int h2, final int w2){
        Pair<Integer, Integer> pair1 = new Pair<>(h1, w1);
        Pair<Integer, Integer> pair2 = new Pair<>(h2, w2);
        Pair<Pair<Integer, Integer>, Pair<Integer,Integer>> pair = new Pair<>(pair1, pair2);

        mBoardLayout.addNewConnection(pair, Color.RED);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setVerUnSelected(h1, w1, h2, w2);
            }
        }, 100);
    }

    public void setmModel(Model mModel) {
        this.mModel = mModel;
    }

    public void setBlock(int w, int h, int val){
        grid[w + 5 * h].setClickable(false);
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.mipmap.tile_block));
    }

    public void clearLines(){
        mBoardLayout.clear();
    }
}
