package com.htnguyen.ihealthclub.view.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.view.adapter.HomePagerFragmentAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main_screen.*


class MainScreenActivity : AppCompatActivity() {

    private val titles = arrayOf("Home", "Friend", "Watch", "Notification", "Person")
    private val listIcon = arrayOf(
        R.drawable.ic_home,
        R.drawable.ic_friend,
        R.drawable.ic_watch,
        R.drawable.ic_notify,
        R.drawable.ic_user
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_screen)
        initView()

    }

    private fun initView() {
        view_pager.adapter = HomePagerFragmentAdapter(titles, this@MainScreenActivity)
        view_pager.isUserInputEnabled = false
        setTabLayout()
    }

    private fun setTabLayout() {
        for (index in listIcon.indices) {
            val tabView = LayoutInflater.from(this@MainScreenActivity).inflate(R.layout.item_tablayout, tab_layout, false)
            tabView.findViewById<TextView>(R.id.name).text = titles[index]
            tabView.findViewById<ImageView>(R.id.icon).setImageDrawable(resources.getDrawable(listIcon[index], null))
            if (index == 0) {
                tabView.findViewById<TextView>(R.id.name).visibility = View.VISIBLE
                tabView.findViewById<ImageView>(R.id.icon)?.setColorFilter(ContextCompat.getColor(this@MainScreenActivity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            tab_layout.addTab(tab_layout.newTab().setCustomView(tabView))
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.name)?.visibility = View.VISIBLE
                tab?.customView?.findViewById<ImageView>(R.id.icon)?.setColorFilter(ContextCompat.getColor(this@MainScreenActivity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
                tab?.position.let { it?.let { it1 -> view_pager.setCurrentItem(it1, false) } }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.name)?.visibility = View.GONE
                tab?.customView?.findViewById<ImageView>(R.id.icon)?.setColorFilter(ContextCompat.getColor(this@MainScreenActivity, R.color.general_bull), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    fun setCurrentFragment(position: Int) {
        view_pager.setCurrentItem(position, false)
    }
}



