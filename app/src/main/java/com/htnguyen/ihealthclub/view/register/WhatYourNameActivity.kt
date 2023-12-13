package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import kotlinx.android.synthetic.main.activity_what_your_name.*


class WhatYourNameActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_your_name)

        btn_next.setOnClickListener{
            val firstName = ed_first_name.text.toString().trim()
            val lastName = ed_last_name.text.toString().trim()

            if (firstName.isNotEmpty() && lastName.isNotEmpty()){
                val user = User( firstName =  firstName,lastName = lastName,
                    phoneNumber = "", gender = 0, birthday = "", email = "", password = "", photoUrl = ""
                )

                val bundle = Bundle()
                bundle.putSerializable(KEY_USER,user)

                val i = Intent(this, BirthdayActivity::class.java)
                i.putExtras(bundle)
                startActivity(i)
            }else{
                Toast.makeText(this,"Please enter first name and last name",Toast.LENGTH_SHORT).show()
            }

        }

//        til_first_name.setEndIconOnClickListener {
//            ed_first_name.setText("")
//        }

        im_back.setOnClickListener {
            finish()
        }
    }
}