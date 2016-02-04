package com.app.william.tribs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;


public class Board extends ActionBarActivity implements View.OnClickListener {

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
    private static Button grid[] = new Button[25];
    private static Button answers[] = new Button[8];
    private static View connectors_hor[] = new View[20];
    private static View connectors_ver[] = new View[20];
    private TextView levelLbl;
    private static String TRIBS_PREFS = "Tribs_Prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.level_picker, null);

        actionBar.setCustomView(view);

        Button nextLvl = (Button) view.findViewById(R.id.nextLvl);
        Button preLvl = (Button) view.findViewById(R.id.prevLvl);
        Button refresh = (Button) view.findViewById(R.id.refresh);

        levelLbl = (TextView) view.findViewById(R.id.lvlTitle);

        nextLvl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                model.increaseLevel();
            }
        });

        preLvl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                model.decreaseLevel();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                model.repeatLevel();
            }
        });

        grid = new Button[25];
        answers = new Button[8];

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                grid[i+ 5 *j] = (Button) findViewById(BOARD_IDS[i + 5*j]);
                grid[i + 5 * j].setOnClickListener(this);
                grid[i+ 5 * j].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blank_with_downstate));
            }
        }

        for(int i =0 ; i < 4; i++){
            for(int j = 0; j < 2; j++){
                answers[i+ 4 *j] = (Button) findViewById(ANSWER_IDS[i + 4*j]);
                answers[i+ 4 * j].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_white));
            }
        }

        for(int i=0; i < 20; i++){
            connectors_hor[i] = findViewById(CONNECTOR_IDS_HORS[i]);
            connectors_ver[i] = findViewById(CONNECTOR_IDS_VER[i]);
        }

        model = new Model(this, this);


        SharedPreferences preferences = getSharedPreferences(TRIBS_PREFS, 0);
        if(preferences.getBoolean("first_time", true) || true) {
            preferences.edit().putBoolean("first_time", false).commit();
            model.startTutorial();
        }else {
            model.startlevel(1);
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

    @Override
    public void onClick(View v){
        int id = v.getId();

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(BOARD_IDS[i + j *5] == id) {
                    model.blockSelected(i, j);
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
            ((LinearLayout)findViewById(R.id.answers_row_two)).invalidate();
        }
    }

    public void setAnswerVisible(int count){
        answers[count].setVisibility(View.VISIBLE);
        if(count > 3){
            ((LinearLayout)findViewById(R.id.answers_row_two)).invalidate();
        }
    }

    public void setTitle(int level){
        if(level ==0) {
            levelLbl.setText("Tutorial");
        }else{
            levelLbl.setText("Level " + level);
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
}
