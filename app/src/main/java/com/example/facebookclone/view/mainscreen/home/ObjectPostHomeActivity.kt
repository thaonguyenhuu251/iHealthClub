package com.example.facebookclone.view.mainscreen.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.facebookclone.R
import kotlinx.android.synthetic.main.activity_object_post_home.*

class ObjectPostHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_post_home)
        initView()
    }
    private fun initView(){
        btn_next.setOnClickListener {
            finish()
        }

        iv_back.setOnClickListener {
            finish()
        }

        rb_public.setOnCheckedChangeListener { compoundButton, checked ->
            if(checked){
                rb_friend.isChecked = false
                rb_friend_restriction.isChecked= false
                rb_friend_specifically.isChecked = false
                rb_private.isChecked = false
            }
        }

        rb_friend.setOnCheckedChangeListener { compoundButton, checked ->
            if(checked){
                rb_public.isChecked = false
                rb_friend_restriction.isChecked= false
                rb_friend_specifically.isChecked = false
                rb_private.isChecked = false
            }
        }

        rb_friend_restriction.setOnCheckedChangeListener { compoundButton, checked ->
            if(checked){
                rb_friend.isChecked = false
                rb_public.isChecked= false
                rb_friend_specifically.isChecked = false
                rb_private.isChecked = false
            }
        }

        rb_friend_specifically.setOnCheckedChangeListener { compoundButton, checked ->
            if(checked){
                rb_friend.isChecked = false
                rb_friend_restriction.isChecked= false
                rb_public.isChecked = false
                rb_private.isChecked = false
            }
        }

        rb_private.setOnCheckedChangeListener { compoundButton, checked ->
            if(checked){
                rb_friend.isChecked = false
                rb_friend_restriction.isChecked= false
                rb_friend_specifically.isChecked = false
                rb_public.isChecked = false
            }
        }

    }
}