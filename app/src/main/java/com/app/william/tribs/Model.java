package com.app.william.tribs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.View;

import com.github.amlcurran.showcaseview.MaterialShowcaseDrawer;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Williamv on 1/3/2016.
 */
public class Model implements Serializable{
    private int MAX_LEVEL = 9;
    private BoardFragment mView;
    private Board mBoard;
    private int mLevel;
    private int numSelected = 0;
    private List<Pair<Integer, Integer>> prevSelected;
    private List<Pair<Integer, Boolean>> answers;
    private List<Integer> grid;
    private Context mContext;
    private int numAnswered;
    private int coachMarksSeen;

    Model(Board v, Context context){
        mBoard = v;
        mContext = context;
    }

    public void startlevel(int l, BoardFragment boardFragment){
        if (l > MAX_LEVEL) return;

        mView = boardFragment;
        mLevel = l;
        numAnswered = 0;
        numSelected = 0;
        prevSelected = new ArrayList<>();
        answers = new ArrayList<> ();
        grid = new ArrayList<>();
        mBoard.setTitle(l);
        List<Integer> temp = new ArrayList<>();
        try {
            Scanner in = new Scanner(mContext.getResources().openRawResource(mLevelFiles[mLevel]));

            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    grid.add(in.nextInt());
                }
            }

            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    mView.setGrid(i, j, grid.get(i + 5 * j));
                }
            }

            for(int i = 0; i < 8; i++) {
                if(in.hasNext()) {
                    int j = in.nextInt();

                    temp.add(j);
                }
            }

            Collections.sort(temp);

            for(int i = 0; i< 8; i++){
                if(i < temp.size()){
                    answers.add(new Pair<>(temp.get(i), true));
                    mView.setAnswerVisible(i);
                    mView.setAnswer(i, temp.get(i));
                }else{
                    mView.setAnswerUnVisible(i);
                }
            }



            for(int i = 0; i < 20; i++){
                mView.setHorsUnSelected(i);
                mView.setVerUnSelected(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockSelected(int r, int c){
        boolean add = true;
        switch (numSelected){
            case 0:
                mView.setButtonSelected(r,c);
                numSelected++;
                break;
            case 1:
                Pair<Integer, Integer> p = prevSelected.get(0);
                if (p.first ==  r && p.second == c) {
                    add = false;
                } else if ((p.first == r + 1 || p.first == r - 1 || p.first == r) && (p.second == c+ 1
                    || p.second == c-1 || p.second == c) ){
                    numSelected++;
                    if(checkHorizontal(p, r, c)) mView.setHorsSelected(p.first, p.second, r, c);
                    if(checkVertical(p, r, c)) mView.setVerSelected(p.first, p.second, r, c);
                } else {
                    numSelected = 1;
                    mView.setWrong(p.first, p.second);
                    prevSelected.clear();
                }
                mView.setButtonSelected(r, c);
                break;
            case 2:
                Pair<Integer, Integer> p2 = prevSelected.get(0);
                Pair<Integer, Integer> p1 = prevSelected.get(1);
                Pair<Integer, Integer> p3 = new Pair<>(r,c);
                if(p3.equals(p1) || p3.equals(p2)){
                    add = false;
                }else if((checkHorizontal(p1,p2, r,c) || checkVertical(p1, p2, r, c) || checkDiagonal(p1,p2, r, c)) && !(prevSelected.contains(new Pair<>(r,c)))){
                    prevSelected.add(p3);
                    checkAnswer();
                    numSelected = 0;
                    prevSelected.clear();
                    add = false;
                }else{
                    numSelected = 1;
                    mView.setWrong(p1.first, p1.second);
                    mView.setWrong(p2.first, p2.second);
                    if(checkHorizontal(p1, p2.first, p2.second)) mView.setHorsWrong(p1.first, p1.second, p2.first, p2.second);
                    if(checkVertical(p1, p2.first, p2.second)) mView.setVerWrong(p1.first, p1.second, p2.first, p2.second);
                    mView.setButtonSelected(r,c);
                    prevSelected.clear();
                }

                break;
        }
        if (add) prevSelected.add(new Pair<>(r, c));

    }

    private boolean checkHorizontal(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2, int r, int c){
        return checkHorizontal(p1, p2.first, p2.second) && (checkHorizontal(p1, r, c) || checkHorizontal(p2, r, c));
    }

    private boolean checkHorizontal(Pair<Integer, Integer> p, int r, int c){
        return (p.second == c && (p.first == r + 1 || p.first == r -1));
    }

    private boolean checkVertical(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2, int r, int c){
        return checkVertical(p1, p2.first, p2.second) && (checkVertical(p1, r, c) || checkVertical(p2, r, c));
    }

    private boolean checkVertical(Pair<Integer, Integer> p1, int r, int c){
        return ((p1.first == r) && (p1.second == c -1 || p1.second == c + 1));
    }

    private boolean checkDiagonal(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2, int r, int c){
        return (checkDiagonalRightUp(p1, p2.first, p2.second) && (checkDiagonalLeftDown(p1, r, c) || checkDiagonalRightUp(p2, r, c)))
                ||(checkDiagonalLeftUp(p1, p2.first, p2.second) && (checkDiagonalRightDown(p1, r, c) || checkDiagonalLeftUp(p2, r, c)))
                || (checkDiagonalLeftDown(p1, p2.first, p2.second) && (checkDiagonalRightUp(p1, r, c) || checkDiagonalLeftDown(p2, r, c)))
                || (checkDiagonalRightDown(p1, p2.first, p2.second) && (checkDiagonalLeftUp(p1, r, c) || checkDiagonalRightDown(p2, r, c)));
    }

    private boolean checkDiagonal(Pair<Integer, Integer> p1, int r, int c){
        return (p1.first == r + 1 || p1.first == r - 1) && (p1.second == c + 1 || p1.second == c -1);
    }

    private boolean checkDiagonalRightUp(Pair<Integer, Integer> p1, int r, int c){
        return (p1.first == r + 1) && (p1.second == c + 1);
    }

    private boolean checkDiagonalRightDown(Pair<Integer, Integer> p1, int r, int c){
        return (p1.first == r + 1) && (p1.second == c - 1);
    }

    private boolean checkDiagonalLeftUp(Pair<Integer, Integer> p1, int r, int c){
        return ( p1.first == r - 1) && (p1.second == c +1);
    }

    private boolean checkDiagonalLeftDown(Pair<Integer, Integer> p1, int r, int c){
        return ( p1.first == r - 1) && (p1.second == c -1);
    }

    public void checkAnswer(){
        Pair<Integer, Integer> p1 = prevSelected.get(0);
        Pair<Integer, Integer> p2 = prevSelected.get(1);
        Pair<Integer, Integer> p3 = prevSelected.get(2);
        int num1 = grid.get(prevSelected.get(0).first + 5 * prevSelected.get(0).second);
        int num2 = grid.get(prevSelected.get(1).first + 5 * prevSelected.get(1).second);
        int num3 = grid.get(prevSelected.get(2).first + 5 * prevSelected.get(2).second);

        int guessAdd = num1 * num2 + num3;
        int guessSub = num1 * num2 - num3;

        boolean correctAdd = false;
        int countAdd = 0;
        for(Pair<Integer, Boolean> p : answers){
            if(guessAdd == p.first && p.second){
                correctAdd = true;
                answers.set(countAdd, new Pair<>(answers.get(countAdd).first, false));

                break;
            }
            countAdd++;
        }
        boolean dialog = false;
        boolean correctSub = false;
        int countSub = 0;
        for(Pair<Integer, Boolean> i : answers){
            if(guessSub == i.first && i.second){
                correctSub = true;
                if(correctAdd){
                    newDialog(guessAdd, guessSub, countAdd, countSub);
                    dialog = true;
                    correctAdd = false;
                    correctSub = false;
                } else{
                    mView.setAnswered(countSub);
                    answers.set(countSub, new Pair<>(answers.get(countSub).first, false));
                }
                break;
            }
            countSub++;
        }
        if(correctAdd) mView.setAnswered(countAdd);
        if(correctSub) mView.setAnswered(countSub);

        if(correctAdd || correctSub || dialog){
            mView.setButtonAnswered(p1.first, p1.second);
            mView.setButtonAnswered(p2.first, p2.second);
            mView.setButtonAnswered(p3.first, p3.second);
            if(checkHorizontal(p1, p2.first, p2.second)) mView.setHorsAnswered(p1.first, p1.second, p2.first, p2.second);
            if(checkHorizontal(p1, p3.first, p3.second)) mView.setHorsAnswered(p1.first, p1.second, p3.first, p3.second);
            if(checkHorizontal(p2, p3.first, p3.second)) mView.setHorsAnswered(p2.first, p2.second, p3.first, p3.second);
            if(checkVertical(p1, p2.first, p2.second)) mView.setVerAnswered(p1.first, p1.second, p2.first, p2.second);
            if(checkVertical(p1, p3.first, p3.second)) mView.setVerAnswered(p1.first, p1.second, p3.first, p3.second);
            if(checkVertical(p2, p3.first, p3.second)) mView.setVerAnswered(p2.first, p2.second, p3.first, p3.second);
            numAnswered++;
            checkWin();
        }else{
            mView.setWrong(p1.first, p1.second);
            mView.setWrong(p2.first, p2.second);
            mView.setWrong(p3.first, p3.second);
            if(checkHorizontal(p1, p2.first, p2.second)) mView.setHorsWrong(p1.first, p1.second, p2.first, p2.second);
            if(checkHorizontal(p1, p3.first, p3.second)) mView.setHorsWrong(p1.first, p1.second, p3.first, p3.second);
            if(checkHorizontal(p2, p3.first, p3.second)) mView.setHorsWrong(p2.first, p2.second, p3.first, p3.second);
            if(checkVertical(p1, p2.first, p2.second)) mView.setVerWrong(p1.first, p1.second, p2.first, p2.second);
            if(checkVertical(p1, p3.first, p3.second)) mView.setVerWrong(p1.first, p1.second, p3.first, p3.second);
            if(checkVertical(p2, p3.first, p3.second)) mView.setVerWrong(p2.first, p2.second, p3.first, p3.second);
        }
    }

    public void newDialog(int guess1, int guess2, final int countAdd, final int countSub){
        new AlertDialog.Builder(mContext)
                .setTitle("Select Guess")
                .setMessage("Which guess do you want?")
                .setPositiveButton(Integer.toString(guess1), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mView.setAnswered(countAdd);
                    }
                })
                .setNegativeButton(Integer.toString(guess2), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        answers.set(countAdd, new Pair<>(answers.get(countAdd).first, true));
                        answers.set(countSub, new Pair<>(answers.get(countSub).first, false));
                        mView.setAnswered(countSub);
                    }
                })
                .show();

    }

    private void checkWin(){
        if(numAnswered == answers.size()){
            endLevelDialog(mLevel);
        }
    }

    public void increaseLevel(){
        if(mLevel + 1 <= MAX_LEVEL){
            mLevel++;
            mBoard.setLevel(mLevel);
        }
    }

    public void decreaseLevel(){
        if(mLevel - 1 >= 1){
            mLevel--;
            mBoard.setLevel(mLevel);
        }
    }

    public void repeatLevel(){
        startlevel(mLevel, mView);
    }

    public void startTutorial(){
        mBoard.setLevel(0);
        coachMarksSeen = 0;

        new TribsTutorial(mContext, this);
    }

    public boolean isBlockSelected(int i, int j){
        return prevSelected.contains(new Pair<>(i,j));
    }

    public void endLevelDialog(int level){
        new AlertDialog.Builder(mContext)
                .setTitle("Completed!")
                .setMessage("You have completed level " + level+"!")
                .setPositiveButton("Next Level", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       increaseLevel();
                    }
                })
                .setNegativeButton("Replay Level", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        repeatLevel();
                    }
                })
                .show();

    }

    public void quit(){
        mBoard.onBackPressed();
    }

    public int getMAX_LEVEL(){
        return MAX_LEVEL;
    }

    private static int mLevelFiles[]={
            R.raw.tut,
            R.raw.lvl1,
            R.raw.lvl2,
            R.raw.lvl3,
            R.raw.lvl4,
            R.raw.lvl5,
            R.raw.lvl6,
            R.raw.lvl7,
            R.raw.lvl8,
            R.raw.lvl9
    };
}
