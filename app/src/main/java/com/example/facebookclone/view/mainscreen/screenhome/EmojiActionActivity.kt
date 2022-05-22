package com.example.facebookclone.view.mainscreen.screenhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import androidx.constraintlayout.widget.ConstraintSet.GONE

import com.example.facebookclone.R


import com.example.facebookclone.utils.SHARED_PREFERENCES_KEY
import com.example.facebookclone.view.adapter.EmojiActionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.core.view.View
import kotlinx.android.synthetic.main.activity_emoji_action.*


class EmojiActionActivity : AppCompatActivity() {
    private val titles = arrayOf("Emojis", "Actions")
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_action)
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        initView()
    }

    private fun initView(){
        //et_emoji_action.visibility = View.
        //iv_emoji_action.setBackgroundResource(sharedPreferences.getInt(ICON_GET, 0))
        view_pager_emoji.adapter = EmojiActionPagerAdapter(titles,this)
        TabLayoutMediator(tab_layout_ea, view_pager_emoji){tab,position ->
            when(position){
                0->{
                    tab.setText("Emoji")
                }
                1->{
                    tab.setText("Action")
                }

            }

        }.attach()

        tab_layout_ea.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0->{
                        tv_back.text = "What do you feel?"
                    }

                    1->{
                        tv_back.text = "What are you doing? "
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        iv_back.setOnClickListener{
            finish()
        }
    }
}