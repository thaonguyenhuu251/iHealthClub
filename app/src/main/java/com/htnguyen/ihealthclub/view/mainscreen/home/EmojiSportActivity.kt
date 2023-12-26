package com.htnguyen.ihealthclub.view.mainscreen.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.EmojiSportHome
import com.htnguyen.ihealthclub.utils.KEY_EMOJI_PUT
import com.htnguyen.ihealthclub.utils.KEY_SPORT_PUT
import com.htnguyen.ihealthclub.view.adapter.PostEmojiAdapter
import kotlinx.android.synthetic.main.activity_emoji_sport.*

class EmojiSportActivity : AppCompatActivity() {
    private val listSport = mutableListOf<EmojiSportHome>()
    private val listEmoji = mutableListOf<EmojiSportHome>()
    private val sportsAdapter by lazy {
        PostEmojiAdapter(this, listSport, callback = { actionHome ->
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_SPORT_PUT, actionHome)
            setResult(RESULT_OK, resultIntent)
            finish()
        })
    }

    private val emojisAdapter by lazy {
        PostEmojiAdapter(this, listEmoji, callback = { emojiHome ->
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_EMOJI_PUT, emojiHome)
            setResult(RESULT_OK, resultIntent)
            finish()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_sport)
        listSport()
        listEmojis()
        val dataEmoji = intent.getSerializableExtra(KEY_EMOJI_PUT) as? EmojiSportHome
        if (dataEmoji != null) {
            et_emoji_sport.setText(dataEmoji.srcImage + "Felling " + dataEmoji.emojiName)
        }
        initView()
        initRecycleView()
    }

    private fun initRecycleView() {
        rcvFeelActivity.layoutManager = GridLayoutManager(this, 2)
        rcvFeelActivity.setHasFixedSize(true)
        rcvFeelActivity.adapter = emojisAdapter
    }


    private fun initView() {

        radioEmoji.setOnClickListener {
            tvBack.text = "What do you feel?"
            rcvFeelActivity.adapter = emojisAdapter

        }

        radioActivity.setOnClickListener {
            tvBack.text = "What are you doing? "
            rcvFeelActivity.adapter = sportsAdapter

        }

        im_back.setOnClickListener {
            finish()
        }


        et_emoji_sport.doOnTextChanged { _, _, _, _ ->
            if (radioEmoji.isChecked) {
                emojisAdapter.filterList(et_emoji_sport.text.toString(), listEmoji)
            } else {
                sportsAdapter.filterList(et_emoji_sport.text.toString(), listSport)
            }
        }

    }

    private fun listEmojis(): MutableList<EmojiSportHome> {
        listEmoji.add(EmojiSportHome(emojiName = "Grinning Face", srcImage = "\uD83D\uDE00"))
        listEmoji.add(EmojiSportHome(emojiName = "Cold Sweat", srcImage = "\uD83E\uDD76"))
        listEmoji.add(EmojiSportHome(emojiName = "Confounded", srcImage = "\uD83D\uDE1D"))
        listEmoji.add(EmojiSportHome(emojiName = "Crying", srcImage = "\uD83D\uDE07"))
        listEmoji.add(EmojiSportHome(emojiName = "Crying Run", srcImage = "\uD83D\uDE02"))
        listEmoji.add(EmojiSportHome(emojiName = "Drooling", srcImage = "\uD83E\uDEE0"))
        listEmoji.add(EmojiSportHome(emojiName = "Eye Roll", srcImage = "\uD83D\uDE09"))
        listEmoji.add(EmojiSportHome(emojiName = "Head Bandage", srcImage = "\uD83E\uDD70"))
        listEmoji.add(EmojiSportHome(emojiName = "Thermometer", srcImage = "\uD83D\uDE0D"))
        listEmoji.add(EmojiSportHome(emojiName = "Fearful", srcImage = "\uD83E\uDD29"))
        listEmoji.add(EmojiSportHome(emojiName = "Grin macing", srcImage = "\uD83D\uDE18"))
        listEmoji.add(EmojiSportHome(emojiName = "Heart Eyes", srcImage = "\uD83E\uDD72"))
        listEmoji.add(EmojiSportHome(emojiName = "hungry", srcImage = "\uD83D\uDE0B"))
        listEmoji.add(EmojiSportHome(emojiName = "Hushed", srcImage = "\uD83D\uDE0B"))
        listEmoji.add(EmojiSportHome(emojiName = "loudly Face", srcImage = "\uD83E\uDD2A"))
        listEmoji.add(EmojiSportHome(emojiName = "Nerd", srcImage = "\uD83E\uDD17"))
        listEmoji.add(EmojiSportHome(emojiName = "Sick", srcImage = "\uD83E\uDD28"))
        listEmoji.add(EmojiSportHome(emojiName = "Sleeping", srcImage = "\uD83E\uDD15"))
        listEmoji.add(EmojiSportHome(emojiName = "Sleeping with Snoring", srcImage = "\uD83E\uDD75"))
        listEmoji.add(EmojiSportHome(emojiName = "Slightly Smiling", srcImage = "\uD83D\uDE37"))
        listEmoji.add(EmojiSportHome(emojiName = "Smiling with Blushed", srcImage = "\uD83D\uDE24"))
        listEmoji.add(EmojiSportHome(emojiName = "Smiling with Halo", srcImage = "\uD83D\uDE31"))
        listEmoji.add(EmojiSportHome(emojiName = "Smirk", srcImage = "\uD83E\uDD73"))
        listEmoji.add(EmojiSportHome(emojiName = "Sunglasses", srcImage = "\uD83E\uDDD0"))
        listEmoji.add(EmojiSportHome(emojiName = "Tears of Joy", srcImage = "\uD83D\uDE44"))
        listEmoji.add(EmojiSportHome(emojiName = "Thinking", srcImage = "\uD83E\uDD7A"))
        listEmoji.add(EmojiSportHome(emojiName = "Very Angry", srcImage = "\uD83E\uDD71"))
        listEmoji.add(EmojiSportHome(emojiName = "Very Mad", srcImage = "\uD83E\uDD11"))
        listEmoji.add(EmojiSportHome(emojiName = "Very Sad", srcImage = "\uD83D\uDE32"))
        listEmoji.add(EmojiSportHome(emojiName = "Zipper Mouth", srcImage = "\uD83E\uDD2F"))
        return listEmoji
    }

    private fun listSport(): MutableList<EmojiSportHome> {
        listSport.add(EmojiSportHome(emojiName = "Basketball", srcImage = "\uD83C\uDFC0"))
        listSport.add(EmojiSportHome(emojiName = "American Football", srcImage = "\uD83C\uDFC8"))
        listSport.add(EmojiSportHome(emojiName = "Soccer Ball", srcImage = "⚽"))
        listSport.add(EmojiSportHome(emojiName = "Baseball ", srcImage = "⚾ "))
        listSport.add(EmojiSportHome(emojiName = "Softball", srcImage = "\uD83E\uDD4E"))
        listSport.add(EmojiSportHome(emojiName = "Tennis", srcImage = "\uD83C\uDFBE"))
        listSport.add(EmojiSportHome(emojiName = "Volleyball", srcImage = "\uD83C\uDFD0"))
        listSport.add(EmojiSportHome(emojiName = "Rugby Football", srcImage = "\uD83C\uDFC9"))
        listSport.add(EmojiSportHome(emojiName = "Billiards", srcImage = "\uD83C\uDFB1"))
        listSport.add(EmojiSportHome(emojiName = "Ping Pong", srcImage = "\uD83C\uDFD3"))
        listSport.add(EmojiSportHome(emojiName = "Badminton", srcImage = "\uD83C\uDFF8"))
        listSport.add(EmojiSportHome(emojiName = "Ice Hockey", srcImage = "\uD83C\uDFD2"))
        listSport.add(EmojiSportHome(emojiName = "Field Hockey", srcImage = "\uD83C\uDFD1"))
        listSport.add(EmojiSportHome(emojiName = "Lacrosse", srcImage = "\uD83E\uDD4D"))
        listSport.add(EmojiSportHome(emojiName = "Cricket Game", srcImage = "\uD83C\uDFCF"))
        listSport.add(EmojiSportHome(emojiName = "Skis", srcImage = "\uD83C\uDFBF"))
        listSport.add(EmojiSportHome(emojiName = "Sled", srcImage = "\uD83D\uDEF7 "))
        listSport.add(EmojiSportHome(emojiName = "Skateboard", srcImage = "\uD83D\uDEF9"))
        listSport.add(EmojiSportHome(emojiName = "Roller Skate", srcImage = "\uD83D\uDEFC"))
        listSport.add(EmojiSportHome(emojiName = "Ice Skate", srcImage = "⛸️"))
        listSport.add(EmojiSportHome(emojiName = "Curling Stone", srcImage = "\uD83E\uDD4C"))
        listSport.add(EmojiSportHome(emojiName = "Direct Hit", srcImage = "\uD83C\uDFAF"))
        listSport.add(EmojiSportHome(emojiName = "Kite", srcImage = "\uD83E\uDE81"))
        listSport.add(EmojiSportHome(emojiName = "Bowling", srcImage = "\uD83C\uDFB3"))
        listSport.add(EmojiSportHome(emojiName = "Person Climbing", srcImage = " \uD83E\uDDD7"))
        listSport.add(EmojiSportHome(emojiName = "Person Mountain Biking", srcImage = "\uD83D\uDEB5"))
        listSport.add(EmojiSportHome(emojiName = "Horse Racing", srcImage = "\uD83C\uDFC7"))
        listSport.add(EmojiSportHome(emojiName = "Snowboarder ", srcImage = "\uD83C\uDFC2"))
        listSport.add(EmojiSportHome(emojiName = "Person Swimming", srcImage = "\uD83C\uDFCA"))
        listSport.add(EmojiSportHome(emojiName = "Person Playing Water Polo", srcImage = "\uD83E\uDD3D"))
        listSport.add(EmojiSportHome(emojiName = "Person Playing Handball", srcImage = "\uD83E\uDD3E"))
        listSport.add(EmojiSportHome(emojiName = "Person Golfing", srcImage = "\uD83C\uDFCC️"))
        listSport.add(EmojiSportHome(emojiName = "Person Surfing", srcImage = "\uD83C\uDFC4"))
        listSport.add(EmojiSportHome(emojiName = "Person Rowing Boat", srcImage = "\uD83D\uDEA3"))
        listSport.add(EmojiSportHome(emojiName = "Person in Lotus Position", srcImage = "\uD83E\uDDD8"))
        listSport.add(EmojiSportHome(emojiName = "Person Lifting Weights", srcImage = "\uD83C\uDFCB️"))
        listSport.add(EmojiSportHome(emojiName = "Person in Steamy Room", srcImage = "\uD83E\uDDD6"))
        listSport.add(EmojiSportHome(emojiName = "Bow and Arrow", srcImage = "\uD83C\uDFF9"))
        listSport.add(EmojiSportHome(emojiName = "Fishing Pole", srcImage = "\uD83C\uDFA3"))
        listSport.add(EmojiSportHome(emojiName = "People Wrestling", srcImage = "\uD83E\uDD3C"))
        listSport.add(EmojiSportHome(emojiName = "Running Shirt", srcImage = "\uD83C\uDFBD"))
        listSport.add(EmojiSportHome(emojiName = "Person Juggling", srcImage = "\uD83E\uDD39"))
        listSport.add(EmojiSportHome(emojiName = "Ballet Shoes", srcImage = "\uD83E\uDE70"))
        listSport.add(EmojiSportHome(emojiName = "Boxing Glove", srcImage = "\uD83E\uDD4A"))
        listSport.add(EmojiSportHome(emojiName = "Martial Arts Uniform", srcImage = "\uD83E\uDD4B"))
        listSport.add(EmojiSportHome(emojiName = "Goal Net", srcImage = "\uD83E\uDD45"))
        listSport.add(EmojiSportHome(emojiName = "Direct Hit", srcImage = "\uD83C\uDFAF"))
        listSport.add(EmojiSportHome(emojiName = "Military Helmet ", srcImage = "\uD83E\uDE96 "))
        return listSport
    }

}