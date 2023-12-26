package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import com.htnguyen.ihealthclub.BR
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.base.BaseActivity
import com.htnguyen.ihealthclub.databinding.ActivityRegisterClubBinding
import kotlinx.android.synthetic.main.activity_register_club.*

class JoinClubActivity: BaseActivity<ActivityRegisterClubBinding, RegisterViewModel>() {
    override val layout: Int
        get() = R.layout.activity_register_club
    override val viewModel: RegisterViewModel
        get() = RegisterViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_club)
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

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }
}