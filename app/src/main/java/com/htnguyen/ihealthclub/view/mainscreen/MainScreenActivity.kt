package com.htnguyen.ihealthclub.view.mainscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.view.adapter.HomePagerFragmentAdapter
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
    private fun initView() {
        view_pager.adapter = HomePagerFragmentAdapter(titles,this)
        view_pager.isUserInputEnabled = false;
        setTabLayout()
    }
    private fun setTabLayout(){
        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            when(position){
                0 -> {
                    tab.setIcon(R.drawable.ic_home_selected)
                    tab.icon?.setTint(getColor(R.color.general_blue_01))
                    toolbar_home.visibility = View.VISIBLE
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
                        toolbar_home.visibility = View.VISIBLE
                        tab.setIcon(R.drawable.ic_home_selected)
                        tab.icon?.setTint(getColor(R.color.general_blue_01))
                    }
                    1 -> {
                        tab.setIcon(R.drawable.ic_friend_selected)
                        tab.icon?.setTint(getColor(R.color.general_blue_01))
                        //toolbar_home.visibility = View.GONE
                    }
                    2 -> {
                        tab.icon = getDrawable(R.drawable.ic_personal_selected)
                        tab.icon?.setTint(getColor(R.color.general_blue_01))
                        //toolbar_home.visibility = View.GONE
                    }
                    3 -> {
                        tab.icon = getDrawable(R.drawable.ic_video_selected)
                        tab.icon?.setTint(getColor(R.color.general_blue_01))
                        //toolbar_home.visibility = View.GONE
                    }
                    4 -> {
                        tab.icon = getDrawable(R.drawable.ic_notify_selected)
                        tab.icon?.setTint(getColor(R.color.general_blue_01))
                        //toolbar_home.visibility = View.GONE
                    }
                    5 -> {
                        tab.icon = getDrawable(R.drawable.ic_menu_selected)
                        tab.icon?.setTint(getColor(R.color.general_blue_01))
                        //toolbar_home.visibility = View.GONE
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

    fun setCurrentFragment(position: Int) {
        view_pager.currentItem = position
    }
}



