package com.app.william.tribs.ui_board;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.william.tribs.R;

/**
 * Created by William on 3/9/2016.
 */
public class StartScreenFragment extends Fragment {

    private StartScreenCallBacks mCallBacks;

    public interface StartScreenCallBacks {
        public void startGame();
    }

    private StartScreenCallBacks mDummyCallBacks = new StartScreenCallBacks() {
        @Override
        public void startGame() {
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_starting_page, container, false);

        Button startBtn = (Button) view.findViewById(R.id.start_button);
        startBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundDrawable(getResources().getDrawable(R.drawable.tile_white_pressed));
                        break;
                    case MotionEvent.ACTION_UP:
                            mCallBacks.startGame();
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof StartScreenCallBacks){
            mCallBacks = (StartScreenCallBacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallBacks = mDummyCallBacks;
    }
}
