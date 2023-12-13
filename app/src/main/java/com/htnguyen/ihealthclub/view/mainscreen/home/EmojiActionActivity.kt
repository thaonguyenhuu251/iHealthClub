package com.htnguyen.ihealthclub.view.mainscreen.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.EmojiHome
import com.htnguyen.ihealthclub.utils.Event
import com.htnguyen.ihealthclub.view.adapter.EmojiActionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_emoji_action.*


class EmojiActionActivity : AppCompatActivity() {
    private val titles = arrayOf("Emojis", "Actions")
    private var emojiName: String = ""
    private var emojiIcon: String = ""

    private var position: Int = 0

    var emojiFragment: EmojiFragment = EmojiFragment()
    var actionFragment: ActionFragment = ActionFragment()

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


        et_emoji_action.doOnTextChanged { _, _, _, _ ->
            if (position == 0) {
                Event.searchEmoji(et_emoji_action.text.toString())
            } else {
                Event.searchEmoji(et_emoji_action.text.toString())
            }
        }

    }

}