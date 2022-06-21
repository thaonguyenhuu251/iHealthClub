package com.example.facebookclone.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.facebookclone.view.mainscreen.screenfriend.FriendRequestFragment
import com.example.facebookclone.view.mainscreen.screenhome.HomeFragment
import com.example.facebookclone.view.mainscreen.screenmenu.MenuFragment
import com.example.facebookclone.view.mainscreen.screennotification.NotificationsFragment
import com.example.facebookclone.view.mainscreen.screenpersonal.PersonalProfileFragment
import com.example.facebookclone.view.mainscreen.screenwatch.WatchVideoFragment

class HomePagerFragmentAdapter(val arrayTitle : Array<String>,val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return arrayTitle.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> { return HomeFragment()}
            1 -> return FriendRequestFragment()
            2 -> return PersonalProfileFragment()
            3 -> return WatchVideoFragment()
            4 -> return NotificationsFragment()
            5 -> return MenuFragment()
        }
        return HomeFragment()
    }

}