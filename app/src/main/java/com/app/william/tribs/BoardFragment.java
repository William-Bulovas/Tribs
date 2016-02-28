package com.app.william.tribs;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

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

    private Button grid[] = new Button[25];
    private Button answers[] = new Button[8];
    private View connectors_hor[] = new View[20];
    private View connectors_ver[] = new View[20];
    private LinearLayout answersRowTwo;
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

        for(int i=0; i < 20; i++){
            connectors_hor[i] = view.findViewById(CONNECTOR_IDS_HORS[i]);
            connectors_ver[i] = view.findViewById(CONNECTOR_IDS_VER[i]);
        }

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
        int h = h1;
        if(h2 < h1) h = h2;
        connectors_hor[w1 * 4 + h].setBackgroundColor(Color.parseColor("#66CD00"));
    }
    public void setHorsUnSelected(int h1, int w1, int h2, int w2){
        int h = h1;
        if(h2 < h1) h = h2;
        connectors_hor[w1 * 4 + h].setBackgroundColor(Color.TRANSPARENT);
    }
    public void setHorsUnSelected(int i){
        connectors_hor[i].setBackgroundColor(Color.TRANSPARENT);
    }
    public void setHorsAnswered(int h1, int w1, int h2, int w2){
        int h = h1;
        if(h2 < h1) h = h2;
        connectors_hor[w1 * 4 + h].setBackgroundColor(Color.BLUE);
    }

    public void setHorsWrong(final int h1, final int w1, final int h2, final int w2){
        int h = h1;
        if(h2 < h1) h = h2;
        connectors_hor[w1 * 4 + h].setBackgroundColor(Color.RED);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setHorsUnSelected(h1, w1, h2, w2);
            }
        }, 100);
    }

    public void setVerSelected(int h1, int w1, int h2, int w2){
        int w = w1;
        if(w2 < w1) w = w2;
        connectors_ver[w * 5 + h1].setBackgroundColor(Color.parseColor("#66CD00"));
    }
    public void setVerUnSelected(int h1, int w1, int h2, int w2){
        int w = w1;
        if(w2 < w1) w = w2;
        connectors_ver[w * 5 + h1].setBackgroundColor(Color.TRANSPARENT);
    }
    public void setVerUnSelected(int i){
        connectors_ver[i].setBackgroundColor(Color.TRANSPARENT);
    }
    public void setVerAnswered(int h1, int w1, int h2, int w2){
        int w = w1;
        if(w2 < w1) w = w2;
        connectors_ver[w * 5 + h1].setBackgroundColor(Color.BLUE);
    }

    public void setVerWrong(final int h1, final int w1, final int h2, final int w2){
        int w = w1;
        if(w2 < w1) w = w2;
        connectors_ver[w * 5 + h1].setBackgroundColor(Color.RED);
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
}
