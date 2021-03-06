package com.app.william.tribs.ui_board;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;

import com.app.william.tribs.Model;
import com.app.william.tribs.R;
import com.github.amlcurran.showcaseview.MaterialShowcaseDrawer;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by William on 2/2/2016.
 */
public class TribsTutorial implements OnShowcaseEventListener {
    private Context mContext;
    private int mCoachMarkId;
    private TextPaint mPaint;
    private Model mModel;
    private ShowcaseDrawer drawer;
    private BoardGridAdapter mAdapter;
    private BoardGridAdapter mAnswerAdapter;

    public TribsTutorial(Context context, Model model, BoardGridAdapter adapter, BoardGridAdapter answerAdapter){
        super();

        mContext = context;
        mCoachMarkId = 0;
        mModel = model;

        mPaint = new TextPaint();
        Paint temp = new Paint();
        temp.setColor(Color.BLACK);
        mPaint.set(temp);
        mAdapter = adapter;
        mAnswerAdapter = answerAdapter;

        drawer = new MaterialShowcaseDrawer(mContext.getResources());
        drawer.setBackgroundColour(Color.BLACK);

        ShowcaseView s = new ShowcaseView.Builder((Activity) mContext)
                .setTarget(new ViewTarget(getTutHighlight(mCoachMarkId)))
                .setContentText(getTutText(mCoachMarkId))
                .setShowcaseDrawer(drawer)
                .setShowcaseEventListener(this).build();

        s.setStyle(R.style.CustomShowcaseTheme);
    }

    public TribsTutorial(Context context, Model model, int level){
        super();

        mContext = context;
        if(level == 10) {
            mCoachMarkId = 7;
            mModel = model;

            mPaint = new TextPaint();
            Paint temp = new Paint();
            temp.setColor(Color.BLACK);
            mPaint.set(temp);

            drawer = new MaterialShowcaseDrawer(mContext.getResources());
            drawer.setBackgroundColour(Color.BLACK);

            ShowcaseView s = new ShowcaseView.Builder((Activity) mContext)
                    .setTarget(new ViewTarget(getTutHighlight(mCoachMarkId)))
                    .setContentText(getTutText(mCoachMarkId))
                    .setShowcaseDrawer(drawer)
                    .setShowcaseEventListener(this).build();

            s.setStyle(R.style.CustomShowcaseTheme);
        }
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        mCoachMarkId++;
        if(mCoachMarkId >= 7){
            return;
        }

        completeAction(mCoachMarkId);
        ShowcaseView s = new ShowcaseView.Builder((Activity) mContext)
                .setTarget(new ViewTarget(getTutHighlight(mCoachMarkId)))
                        .setShowcaseDrawer(drawer)
                        .setContentText(getTutText(mCoachMarkId))
                        .setShowcaseEventListener(this).build();

        s.setStyle(R.style.CustomShowcaseTheme);
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    private String getTutText(int id){
        switch (id){
            case 0:
                return "Tribs is a puzzle game where you select 3 tiles in a row to make an answer!";
            case 1:
                return "The first two numbers are multiplied";
            case 2:
                return "And the third tile is added or subtracted";
            case 3:
                return "See! 1 * 2 = 2 + 4 = 6!";
            case 4:
                return "To complete the level you have to get all of the answers highlighted";
            case 5:
                return "You can link any three tiles that are horizontal, vertical, or diagonal!";
            case 6:
                return "Now complete the level!";
            case 7:
                return "These blocks you cannot tap!";
        }
        return "error";
    }

    private View getTutHighlight(int id){
        switch (id){
            case 0:
                return ((Button) mAdapter.getItem(0));
            case 1:
                return ((Button) mAdapter.getItem(5));
            case 2:
                return ((Button) mAdapter.getItem(10));
            case 3:
                return ((Button) mAnswerAdapter.getItem(1));
            case 4:
                return ((Button) mAnswerAdapter.getItem(1));
            case 5:
                return ((Button) mAdapter.getItem(8));
            case 6:
                return ((Button) mAdapter.getItem(8));
            case 7:
                return ((Button) mAdapter.getItem(0));
        }
        return null;
    }

    public void completeAction(int id){
        switch (id){
            case 1:
                if(!mModel.isBlockSelected(0,0)) mModel.blockSelected(0,0);
                break;
            case 2:
                if(!mModel.isBlockSelected(0,1)) mModel.blockSelected(0,1);
                break;
            case 3:
                if(!mModel.isBlockSelected(0,2)) mModel.blockSelected(0,2);
                break;
            case 4:
                break;
            case 5:
                if(!mModel.isBlockSelected(1,3)) mModel.blockSelected(1,3);
                if(!mModel.isBlockSelected(2,2)) mModel.blockSelected(2,2);
                break;
            case 6:
                break;
        }
    }
}
