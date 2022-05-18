package com.example.facebookclone.view.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.facebookclone.R
import kotlinx.android.synthetic.main.activity_forgot_password_mobile.*


class ForgotPasswordMobileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_mobile)

        initView()
        
    }

    private fun initView(){
        btn_find_account.setOnClickListener {

        }

        im_back.setOnClickListener {
            finish()
        }
    }
}