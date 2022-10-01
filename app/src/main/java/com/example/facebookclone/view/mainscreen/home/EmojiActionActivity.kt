package com.example.facebookclone.view.mainscreen.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
import com.example.facebookclone.utils.KEY_SEARCH_EMOJI
import com.example.facebookclone.view.adapter.EmojiActionPagerAdapter
import com.example.facebookclone.view.mainscreen.photoeditor.tools.EditingToolsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_emoji_action.*
import javax.security.auth.callback.Callback
import kotlin.math.log


class EmojiActionActivity : AppCompatActivity() {
    private val titles = arrayOf("Emojis", "Actions")
    private var emojiHome: EmojiHome? = null
    private var emojiName: String = ""
    private var emojiIcon: String = ""

    private var position: Int = 0

    private var searchEmoji: OnEmojiSearch?  = object : OnEmojiSearch{
        override fun onTextChance(search: String) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_action)
        val data = intent.getSerializableExtra(KEY_EMOJI_PUT) as? EmojiHome
        if (data != null) {
            emojiName = "Felling " + data.emojiName
            emojiIcon = data.srcImage
            ln_search_emoji.visibility = View.GONE
            ln_status_emoji.visibility = View.VISIBLE
            tv_icon_emoji.text = emojiIcon
            tv_status_emoji.text = emojiName
        }
        initView()
    }

    private fun initView() {
        view_pager_emoji.adapter = EmojiActionPagerAdapter(titles, this)
        TabLayoutMediator(tab_layout_ea, view_pager_emoji) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Emoji"
                }
                1 -> {
                    tab.text = "Action"
                }
            }

        }.attach()

        tab_layout_ea.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tv_back.text = "What do you feel?"
                        position = tab.position
                    }

                    1 -> {
                        tv_back.text = "What are you doing? "
                        position = tab.position
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        iv_status_emoji.setOnClickListener {
            tv_icon_emoji.text = ""
            tv_status_emoji.text = ""
            ln_status_emoji.visibility = View.GONE
            ln_search_emoji.visibility = View.VISIBLE
        }

        iv_back.setOnClickListener {
            finish()
        }

        et_emoji_action.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (position == 0){
                    Log.d("nht", "afterTextChanged: "+ s.toString())
                    searchEmoji?.onTextChance(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }
}