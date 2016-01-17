package com.app.william.tribs;

import android.content.Context;
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
    private Board mView;
    private int mLevel;
    private int numSelected = 0;
    private List<Pair<Integer, Integer>> prevSelected;
    private List<Pair<Integer, Boolean>> answers;
    private List<Integer> grid;
    private Context mContext;

    Model(Board v, Context context){
        mView = v;
        mContext = context;
    }

    public void startlevel(int l){
        mLevel = l;

        prevSelected = new ArrayList<>();
        answers = new ArrayList<> ();
        grid = new ArrayList<>();

        try {
        Scanner in = new Scanner(mContext.getResources().openRawResource(R.raw.lvl1));

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
                answers.add(new Pair<>(in.nextInt(), true));
            }

            for(int i = 0; i< 4; i++){
                for(int j = 0; j < 2; j++){
                    mView.setAnswer(i,j, answers.get(i + j * 4).first);
                }
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
                if ((p.first == r + 1 || p.first == r - 1 || p.first == r) && (p.second == c+ 1
                    || p.second == c-1 || p.second == c) && (!(p.first ==  r && p.second == c))){
                        numSelected++;
                } else{
                    numSelected = 1;
                    mView.unSetButtonSelected(p.first, p.second);
                    prevSelected.clear();
                }
                mView.setButtonSelected(r,c);
                break;
            case 2:
                Pair<Integer, Integer> p2 = prevSelected.get(0);
                Pair<Integer, Integer> p1 = prevSelected.get(1);
                if (((p1.first == r + 1 || p1.first == r - 1 || p1.first == r) && (p1.second == c+ 1
                        || p1.second == c-1 || p1.second == c) && (!(p1.first ==  r && p1.second == c)))
                        || ((p2.first == r + 1 || p2.first == r - 1 || p2.first == r) && (p2.second == c + 1
                        || p2.second == c - 1 || p2.second == c) && (!(p2.first ==  r && p2.second == c)))){
                    prevSelected.add(new Pair<Integer, Integer>(r,c));
                    if(checkAnswer()){
                        mView.setButtonAnswered(p1.first, p1.second);
                        mView.setButtonAnswered(p2.first, p2.second);
                        mView.setButtonAnswered(r, c);
                    } else{
                        mView.unSetButtonSelected(p1.first, p1.second);
                        mView.unSetButtonSelected(p2.first, p2.second);
                    }
                    numSelected = 0;
                    prevSelected.clear();
                    add = false;

                }else{
                    numSelected = 1;
                    mView.unSetButtonSelected(p1.first, p1.second);
                    mView.unSetButtonSelected(p2.first, p2.second);
                    mView.setButtonSelected(r,c);
                    prevSelected.clear();
                }

                break;
        }
        if (add) prevSelected.add(new Pair<Integer, Integer>(r, c));

    }

    public boolean checkAnswer(){
        int num1 = grid.get(prevSelected.get(0).first + 5 * prevSelected.get(0).second);
        int num2 = grid.get(prevSelected.get(1).first + 5 * prevSelected.get(1).second);
        int num3 = grid.get(prevSelected.get(2).first + 5 * prevSelected.get(2).second);

        int guessAdd = num1 * num2 + num3;
        int guessSub = num1 * num2 - num3;

        boolean correctAdd = false;
        Pair correctPairAdd;
        for(Pair<Integer, Boolean> p : answers){
            if(guessAdd == p.first && p.second){
                correctAdd = true;
                correctPairAdd = p;
                //p.second = false;
                break;
            }
        }

        boolean correctSub = false;
        for(Pair<Integer, Boolean> i : answers){
            if(guessSub == i.first && i.second){
                correctSub = true;
                if(correctAdd){
                    if(mView.newDialog(guessAdd, guessSub)){

                    }else{
                        //i.second = false;
                        //correctPairAdd.second = true;
                    }
                }
                break;
            }
        }

        return correctAdd || correctSub;
    }
}
