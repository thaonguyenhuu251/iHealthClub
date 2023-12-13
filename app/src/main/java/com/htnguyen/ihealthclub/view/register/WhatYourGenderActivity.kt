package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import kotlinx.android.synthetic.main.activity_what_your_gender.*

class WhatYourGenderActivity:AppCompatActivity() {

    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_your_gender)

        Log.d("atvbirthday", "onCreate: + ${intent.extras?.get(KEY_USER)}")

        user = intent.extras?.get(KEY_USER) as User

        btn_next.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER,user)
            val i = Intent(this@WhatYourGenderActivity, ContactNumberActivity::class.java)
            i.putExtras(bundle)
            startActivity(i)


        }

        rb_female.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked){
                rb_male.isChecked = false
                rb_custom.isChecked = false
                user?.gender=0
            }
        }

        rb_male.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked){
                rb_female.isChecked = false
                rb_custom.isChecked = false
                user?.gender=1
            }
        }


        rb_custom.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked){
                rb_male.isChecked = false
                rb_female.isChecked = false
                user?.gender=2
            }
        }


        im_back.setOnClickListener {
            finish()
        }
    }
}