package com.htnguyen.ihealthclub.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.htnguyen.ihealthclub.view.mainscreen.home.ActionFragment
import com.htnguyen.ihealthclub.view.mainscreen.home.EmojiFragment

class EmojiActionPagerAdapter(val arrayTitle : Array<String>,val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return arrayTitle.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return EmojiFragment()
            1 -> return ActionFragment()
        }
        return EmojiFragment()
    }
}