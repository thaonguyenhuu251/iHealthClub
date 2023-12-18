package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import kotlinx.android.synthetic.main.activity_join_club.*


class JoinClubActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_club)
        btn_next.setOnClickListener {
            val i = Intent(this@JoinClubActivity, WhatYourNameActivity::class.java)
            startActivity(i)
        }

        im_back.setOnClickListener {
            finish()
        }

        tv_back.setOnClickListener {
            finish()
        }
    }
}