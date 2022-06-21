package com.example.facebookclone.view.mainscreen.screenhome
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
import com.example.facebookclone.view.adapter.EmojiActionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_emoji_action.*
import kotlinx.android.synthetic.main.activity_emoji_action.iv_back



class EmojiActionActivity : AppCompatActivity() {
    private val titles = arrayOf("Emojis", "Actions")
    private  var emojiHome: EmojiHome ?= null
    private  var emojiName: String = ""
    private  var emojiIcon: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_action)
        val data = intent.getSerializableExtra(KEY_EMOJI_PUT) as? EmojiHome
        if (data != null){
            emojiName = "Felling " + data.emojiName
            emojiIcon = data.srcImage
            ln_search_emoji.visibility = View.GONE
            ln_status_emoji.visibility = View.VISIBLE
            tv_icon_emoji.text = emojiIcon
            tv_status_emoji.text = emojiName
        }
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
            @SuppressLint("ResourceAsColor")
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

        iv_status_emoji.setOnClickListener {
            tv_icon_emoji.text= ""
            tv_status_emoji.text = ""
            ln_status_emoji.visibility = View.GONE
            ln_search_emoji.visibility = View.VISIBLE
        }

        iv_back.setOnClickListener{
            finish()
        }

    }
}