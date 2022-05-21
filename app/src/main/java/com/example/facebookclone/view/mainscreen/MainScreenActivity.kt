package com.example.facebookclone.view.mainscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.R
import com.example.facebookclone.view.adapter.HomePagerFragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main_screen.*


class MainScreenActivity : AppCompatActivity() {

    private val titles = arrayOf("Movies", "Events", "Tickets", "Tickets", "Tickets", "Tickets")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_screen)

        initView()

    }

    private fun initView(){
//        supportActionBar!!.elevation = 0f

        view_pager.adapter = HomePagerFragmentAdapter(titles,this)

        TabLayoutMediator(tab_layout,view_pager,) { tab, position ->
            when(position){
                0 -> {
                    tab.setIcon(R.drawable.ic_home_selected)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_friend)
                }
                2 -> {
                    tab.setIcon(R.drawable.ic_personal)
                }
                3 -> {
                    tab.setIcon(R.drawable.ic_watch)
                }
                4 -> {
                    tab.setIcon(R.drawable.ic_notify)
                }
                5 -> {
                    tab.setIcon(R.drawable.ic_menu)
                }
            }
        }.attach()

        tab_layout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when(tab?.position){
                    0 -> {
                        tab.setIcon(R.drawable.ic_home_selected)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_friend_selected)
                    }
                    2 -> {
                        tab.setIcon(R.drawable.ic_personal_selected)
                    }
                    3 -> {
                        tab.setIcon(R.drawable.ic_video_selected)
                    }
                    4 -> {
                        tab.setIcon(R.drawable.ic_notify_selected)
                    }
                    5 -> {
                        tab.setIcon(R.drawable.ic_menu_selected)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> {
                        tab.setIcon(R.drawable.ic_home)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_friend)
                    }
                    2 -> {
                        tab.setIcon(R.drawable.ic_personal)
                    }
                    3 -> {
                        tab.setIcon(R.drawable.ic_watch)
                    }
                    4 -> {
                        tab.setIcon(R.drawable.ic_notify)
                    }
                    5 -> {
                        tab.setIcon(R.drawable.ic_menu)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


    }
}


