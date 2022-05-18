package com.example.facebookclone.view.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.example.facebookclone.R

class BottomSheetCustom(context: Context?) : LinearLayout(context) {



    init {
        View.inflate(context, R.layout.bottom_sheet_home, this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}