package com.app.william.tribs.ui_board

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.app.william.tribs.R

/**
 * Created by William on 3/9/2016.
 */
class StartScreenFragment : Fragment() {

    private var mCallBacks: StartScreenCallBacks? = null

    interface StartScreenCallBacks {
        fun startGame()
    }

    private val mDummyCallBacks = object : StartScreenCallBacks {
        override fun startGame() {}
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.activity_starting_page, container, false)

        val startBtn = view.findViewById(R.id.start_button) as Button
        startBtn.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> v.setBackgroundDrawable(resources.getDrawable(R.drawable.tile_white_pressed))
                MotionEvent.ACTION_UP -> mCallBacks!!.startGame()
            }
            true
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is StartScreenCallBacks) {
            mCallBacks = context as StartScreenCallBacks?
        }
    }

    override fun onDetach() {
        super.onDetach()

        mCallBacks = mDummyCallBacks
    }
}
