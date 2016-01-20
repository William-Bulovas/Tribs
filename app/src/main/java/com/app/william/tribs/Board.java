package com.app.william.tribs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;


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

    private Model model;
    private static Button grid[] = new Button[25];
    private static Button answers[] = new Button[8];
    private boolean answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        grid = new Button[25];
        answers = new Button[8];

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                grid[i+ 5 *j] = (Button) findViewById(BOARD_IDS[i + 5*j]);
                grid[i + 5 * j].setOnClickListener(this);
                grid[i+ 5 * j].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_white));
            }
        }

        for(int i =0 ; i < 4; i++){
            for(int j = 0; j < 2; j++){
                answers[i+ 4 *j] = (Button) findViewById(ANSWER_IDS[i + 4*j]);
                answers[i+ 4 * j].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_white));
            }
        }

        model = new Model(this, this);

        model.startlevel(1);
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

    public void setAnswer(int w, int h, int val){
        answers[w+ 4 * h].setText(String.valueOf(val));
        answers[w+4 * h].setClickable(false);
        setUnAnswered(w + 4 *h);
    }

    public void setButtonSelected(int w, int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_green));
    }

    public void unSetButtonSelected(int w, int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_white));
    }

    public void setButtonAnswered(int w, int h){
        grid[w + 5 * h].setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_blue));
        grid[w + 5 * h].setClickable(false);
    }



    public boolean getAnswer(){
        return answer;
    }

    public void setAnswered(int count){
        answers[count].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_gold));
    }

    public void setUnAnswered(int count){
        answers[count].setBackgroundDrawable(getResources().getDrawable(R.drawable.answer_white));
    }

    public void setAnswerUnVisible(int count){
        answers[count].setVisibility(View.GONE);
    }
}
