package com.app.william.tribs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.sql.Statement;

/**
 * Created by William on 2/5/2016.
 */
public class LevelPickerFragment extends Fragment {
    private int mFirstLevel;
    private int mLastLevel;
    private GridView mGridView;
    private StartLevelCallBack mCallBacks;

    public static LevelPickerFragment newInstance(int levelStart, int levelEnd) {
        LevelPickerFragment myFragment = new LevelPickerFragment();

        Bundle args = new Bundle();
        args.putInt("levelStart", levelStart);
        args.putInt("levelEnd", levelEnd);
        myFragment.setArguments(args);

        return myFragment;
    }

    public static interface StartLevelCallBack {
        public void startLevel(int i);
    }

    private static StartLevelCallBack DummyCallBacks = new StartLevelCallBack() {
        @Override
        public void startLevel(int i){}
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFirstLevel = getArguments().getInt("levelStart");
        mLastLevel = getArguments().getInt("levelEnd");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.level_picker_page, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridview_level_picker);

        int width = v.getWidth();
        mGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 16;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {


                Button btn;
                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    btn = new Button(getActivity());
                    btn.setLayoutParams(new GridView.LayoutParams(250, 250));
                    btn.setPadding(4, 4, 4, 4);
                    btn.setText(String.valueOf(position + 1));
                } else {
                    btn = (Button) convertView;
                }

                if(position + 1 > mLastLevel) {
                    btn.setVisibility(View.INVISIBLE);
                }
                else {
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCallBacks.startLevel(position + 1);
                        }
                    });
                }

                btn.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.tile_white));
                return btn;
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof StartLevelCallBack){
            mCallBacks = (StartLevelCallBack) context;
        }

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mCallBacks = DummyCallBacks;

        super.onDetach();
    }
}
