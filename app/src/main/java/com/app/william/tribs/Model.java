package com.app.william.tribs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.View;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Williamv on 1/3/2016.
 */
public class Model {
    private int MAX_LEVEL = 5;
    private Board mView;
    private int mLevel;
    private int numSelected = 0;
    private List<Pair<Integer, Integer>> prevSelected;
    private List<Pair<Integer, Boolean>> answers;
    private List<Integer> grid;
    private Context mContext;
    private int numAnswered;

    Model(Board v, Context context){
        mView = v;
        mContext = context;
    }

    public void startlevel(int l){
        if (l > MAX_LEVEL) return;
        mLevel = l;
        numAnswered = 0;
        numSelected = 0;
        prevSelected = new ArrayList<>();
        answers = new ArrayList<> ();
        grid = new ArrayList<>();
        mView.setTitle(l);

        try {
            Scanner in = new Scanner(mContext.getResources().openRawResource(mLevelFiles[mLevel - 1]));

            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    grid.add(in.nextInt());
                }
            }

            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    mView.setGrid(i, j, grid.get(i + 5*j));
                }
            }

            for(int i = 0; i < 8; i++) {
                if(in.hasNext()) {
                    answers.add(new Pair<>(in.nextInt(), true));
                    mView.setAnswerVisible(i);
                    mView.setAnswer(i, answers.get(i).first);
                } else{
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
        return ((p1.first == p2.first) && (r == p1.first) && ((c == p1.second -1) || (c== p1.second +1) ||
                (c == p2.second -1) || (c== p2.second + 1)));
    }

    private boolean checkHorizontal(Pair<Integer, Integer> p, int r, int c){
        return (p.second == c && (p.first == r + 1 || p.first == r -1));
    }

    private boolean checkVertical(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2, int r, int c){
        return ((p1.second == p2.second) && (c == p1.second) && ((r == p1.first +1 ) || (r == p1.first -1 ) || (r == p2.first - 1) ||
                (r == p2.first +1)));
    }

    private boolean checkVertical(Pair<Integer, Integer> p1, int r, int c){
        return ((p1.first == r) && (p1.second == c -1 || p1.second == c + 1));
    }

    private boolean checkDiagonal(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2, int r, int c){
        return ((((p1.first == p2.first + 1) && ((r == p1.first + 1) || (r == p2.first - 1)))
                || ((p1.first == p2.first - 1) && ((r == p1.first - 1) || (r == p2.first + 1))))
                && (((p1.second == p2.second +1)&& (( c == p1.second + 1) || (c == p2.second -1)))
                || ((p1.second == p2.second - 1) && ((c == p1.second -1) || (c == p2.second + 1)))));
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void checkWin(){
        if(numAnswered == answers.size()){
            startlevel(mLevel + 1);
        }
    }

    public void increaseLevel(){
        if(mLevel + 1 <= MAX_LEVEL){
            mLevel++;
            startlevel(mLevel);
        }
    }

    public void decreaseLevel(){
        if(mLevel - 1 >= 1){
            mLevel--;
            startlevel(mLevel);
        }
    }

    private static int mLevelFiles[]={
            R.raw.lvl1,
            R.raw.lvl2,
            R.raw.lvl3,
            R.raw.lvl4,
            R.raw.lvl5
    };
}
