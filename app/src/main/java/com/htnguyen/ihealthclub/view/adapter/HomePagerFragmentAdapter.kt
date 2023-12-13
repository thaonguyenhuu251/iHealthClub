package com.htnguyen.ihealthclub.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.htnguyen.ihealthclub.view.mainscreen.friend.FriendRequestFragment
import com.htnguyen.ihealthclub.view.mainscreen.menu.MenuFragment
import com.htnguyen.ihealthclub.view.mainscreen.notification.NotificationsFragment
import com.htnguyen.ihealthclub.view.mainscreen.personal.PersonalProfileFragment
import com.htnguyen.ihealthclub.view.mainscreen.home.HomeFragment
import com.htnguyen.ihealthclub.view.mainscreen.watch.WatchVideoFragment

class HomePagerFragmentAdapter(val arrayTitle : Array<String>,val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return arrayTitle.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> { return HomeFragment() }
            1 -> return FriendRequestFragment()
            2 -> return PersonalProfileFragment()
            3 -> return WatchVideoFragment()
            4 -> return NotificationsFragment()
            5 -> return MenuFragment()
        }
        return HomeFragment()
    }
}