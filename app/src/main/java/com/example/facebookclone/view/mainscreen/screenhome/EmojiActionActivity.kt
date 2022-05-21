package com.example.facebookclone.view.mainscreen.screenhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.facebookclone.R
import com.example.facebookclone.view.adapter.EmojiActionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_emoji_action.*
import kotlinx.android.synthetic.main.activity_main_screen.*

class EmojiActionActivity : AppCompatActivity() {
    private val titles = arrayOf("Emojis", "Actions")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_action)
        initView()
    }

    private fun initView(){
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