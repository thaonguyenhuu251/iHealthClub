package com.example.facebookclone.view.mainscreen.screenhome

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
import com.example.facebookclone.model.onItemClickListener
import com.example.facebookclone.utils.*
import com.example.facebookclone.view.adapter.EmojiHomeAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_emoji.*
import kotlinx.android.synthetic.main.item_emoji_home.*
import kotlinx.android.synthetic.main.layout_bottom_create_post.*
import kotlinx.android.synthetic.main.layout_menu_bottom_create_post.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmojiFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var emojisAdapter: EmojiHomeAdapter? = null
    private lateinit var sharedPreferences : SharedPreferences
    private val list = mutableListOf<EmojiHome>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emoji, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        initView()
    }
    private fun initView(){
        initRecycleView()
//        ln_item_emojis.setOnClickListener {
//            val editor = sharedPreferences.edit()
//            editor.putInt(ICON_GET, iv_emojis_home.toString().toInt())
//            editor.putString(ICON_NAME,tv_emojis_home.text.toString())
//            editor.commit()
//        }

    }

    private fun initRecycleView(){
        emojisAdapter = EmojiHomeAdapter(requireContext(), listEmojis = listEmojis()){emojisHome ->

        }

        rv_emoji.layoutManager = GridLayoutManager(requireContext(),2)
        rv_emoji.setHasFixedSize(true)
        rv_emoji.adapter = emojisAdapter

        rv_emoji.setOnClickListener {
            val editor = sharedPreferences.edit()
//           editor.putInt(ICON_GET, iv_emojis_home.toString().toInt())
//           editor.putString(ICON_NAME,tv_emojis_home.text.toString())
//           editor.commit()
        }

        emojisAdapter?.setOnItemClickListener(object : onItemClickListener {
            override fun onItemClick(position: Int) {
                TODO("Not yet implemented")
                Log.d("nht", "init: $position" )
                showSnackBar("Successful")
            }
        })
    }

    private fun listEmojis() :  MutableList<EmojiHome>{
        list.add(EmojiHome(emojiName = "Anguished" , srcImage = R.drawable.emoji_anguished))
        list.add(EmojiHome(emojiName = "Cold Sweat" , srcImage = R.drawable.emoji_cold_sweat))
        list.add(EmojiHome(emojiName = "Confounded" , srcImage = R.drawable.emoji_confounded))
        list.add(EmojiHome(emojiName = "Crying" , srcImage = R.drawable.emoji_crying))
        list.add(EmojiHome(emojiName = "Crying Run" , srcImage = R.drawable.emoji_crying_run))
        list.add(EmojiHome(emojiName = "Drooling" , srcImage = R.drawable.emoji_drooling))
        list.add(EmojiHome(emojiName = "Eye Roll" , srcImage = R.drawable.emoji_eye_roll))
        list.add(EmojiHome(emojiName = "Head Bandage" , srcImage = R.drawable.emoji_head_bandage))
        list.add(EmojiHome(emojiName = "Thermometer" , srcImage = R.drawable.emoji_thermometer))
        list.add(EmojiHome(emojiName = "Fearful" , srcImage = R.drawable.emoji_fearful))
        list.add(EmojiHome(emojiName = "Grinmacing" , srcImage = R.drawable.emoji_grinmacing))
        list.add(EmojiHome(emojiName = "Heart Eyes" , srcImage = R.drawable.emoji_hert_eyes))
        list.add(EmojiHome(emojiName = "hungry" , srcImage = R.drawable.emoji_hungry))
        list.add(EmojiHome(emojiName = "Hushed" , srcImage = R.drawable.emoji_hushed))
        list.add(EmojiHome(emojiName = "loudly Face" , srcImage = R.drawable.emoji_loudly_face))
        list.add(EmojiHome(emojiName = "Nerd" , srcImage = R.drawable.emoji_nerd))
        list.add(EmojiHome(emojiName = "Sick" , srcImage = R.drawable.emoji_sick))
        list.add(EmojiHome(emojiName = "Sleeping" , srcImage = R.drawable.emoji_sleeping))
        list.add(EmojiHome(emojiName = "Sleeping with Snoring" , srcImage = R.drawable.emoji_sleeping_with_snoring))
        list.add(EmojiHome(emojiName = "Slightly Smiling" , srcImage = R.drawable.emoji_slightly_smiling))
        list.add(EmojiHome(emojiName = "Smiling with Blushed" , srcImage = R.drawable.emoji_smiling_with_blushed))
        list.add(EmojiHome(emojiName = "Smiling with Halo" , srcImage = R.drawable.emoji_smiling_with_halo))
        list.add(EmojiHome(emojiName = "Smirk" , srcImage = R.drawable.emoji_smirk))
        list.add(EmojiHome(emojiName = "Sunglasses" , srcImage = R.drawable.emoji_sunglasses))
        list.add(EmojiHome(emojiName = "Tears of Joy" , srcImage = R.drawable.emoji_tears_of_joy))
        list.add(EmojiHome(emojiName = "Thinking" , srcImage = R.drawable.emoji_thinking))
        list.add(EmojiHome(emojiName = "Very Angry" , srcImage = R.drawable.emoji_very_angry))
        list.add(EmojiHome(emojiName = "Very Mad" , srcImage = R.drawable.emoji_very_mad))
        list.add(EmojiHome(emojiName = "Very Sad" , srcImage = R.drawable.emoji_very_sad))
        list.add(EmojiHome(emojiName = "Zipper Mouth" , srcImage = R.drawable.emoji_zipper_mouth))

        return list
    }
    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }
}