package com.htnguyen.ihealthclub.view.mainscreen.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        im_back.setOnClickListener {
            finish()
        }
    }
}