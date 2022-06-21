package com.example.facebookclone.view.mainscreen.screenhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji2.text.EmojiCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
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

const val KEY_EMOJI_PUT = "KEY_EMOJI_PUT"

class EmojiFragment : Fragment() {
    //     TODO: Rename and change types of parameters
//    private var emojisAdapter: EmojiHomeAdapter? = null
    private val emojisAdapter by lazy {
        EmojiHomeAdapter(requireContext(), listEmojis()) { emojiHome ->
            val resultIntent = Intent()

            resultIntent.putExtra(KEY_EMOJI_PUT, emojiHome)
            requireActivity().setResult(AppCompatActivity.RESULT_OK, resultIntent)
            requireActivity().finish()
        }
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val list = mutableListOf<EmojiHome>()

//    private val startForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val intent = result.data
//                val data = intent?.getStringExtra("KEY_PATH_EMOJI")
//
//                Glide.with(this).load(data).into(img_pick)
//            }
//        }

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

    private fun initView() {
        initRecycleView()
//        ln_item_emojis.setOnClickListener {
//            val editor = sharedPreferences.edit()
//            editor.putInt(ICON_GET, iv_emojis_home.toString().toInt())
//            editor.putString(ICON_NAME,tv_emojis_home.text.toString())
//            editor.commit()
//        }

    }

    private fun initRecycleView() {
//        emojisAdapter = EmojiHomeAdapter(requireContext(), listEmojis = listEmojis()){emojisHome ->
//
//        }

        rv_emoji.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_emoji.setHasFixedSize(true)
        rv_emoji.adapter = emojisAdapter


    }

    private fun listEmojis(): MutableList<EmojiHome> {
        list.add(EmojiHome(emojiName = "Grinning Face", srcImage = "\uD83D\uDE00"))
        list.add(EmojiHome(emojiName = "Cold Sweat", srcImage = "\uD83E\uDD76"))
        list.add(EmojiHome(emojiName = "Confounded", srcImage = "\uD83D\uDE1D"))
        list.add(EmojiHome(emojiName = "Crying", srcImage = "\uD83D\uDE07"))
        list.add(EmojiHome(emojiName = "Crying Run", srcImage = "\uD83D\uDE02"))
        list.add(EmojiHome(emojiName = "Drooling", srcImage = "\uD83E\uDEE0"))
        list.add(EmojiHome(emojiName = "Eye Roll", srcImage = "\uD83D\uDE09"))
        list.add(EmojiHome(emojiName = "Head Bandage", srcImage = "\uD83E\uDD70"))
        list.add(EmojiHome(emojiName = "Thermometer", srcImage = "\uD83D\uDE0D"))
        list.add(EmojiHome(emojiName = "Fearful", srcImage = "\uD83E\uDD29"))
        list.add(EmojiHome(emojiName = "Grinmacing", srcImage = "\uD83D\uDE18"))
        list.add(EmojiHome(emojiName = "Heart Eyes", srcImage = "\uD83E\uDD72"))
        list.add(EmojiHome(emojiName = "hungry", srcImage = "\uD83D\uDE0B"))
        list.add(EmojiHome(emojiName = "Hushed", srcImage = "\uD83D\uDE0B"))
        list.add(EmojiHome(emojiName = "loudly Face", srcImage = "\uD83E\uDD2A"))
        list.add(EmojiHome(emojiName = "Nerd", srcImage = "\uD83E\uDD17"))
        list.add(EmojiHome(emojiName = "Sick", srcImage = "\uD83E\uDD28"))
        list.add(EmojiHome(emojiName = "Sleeping", srcImage = "\uD83E\uDD15"))
        list.add(EmojiHome(emojiName = "Sleeping with Snoring", srcImage = "\uD83E\uDD75"))
        list.add(EmojiHome(emojiName = "Slightly Smiling", srcImage = "\uD83D\uDE37"))
        list.add(EmojiHome(emojiName = "Smiling with Blushed", srcImage = "\uD83D\uDE24"))
        list.add(EmojiHome(emojiName = "Smiling with Halo", srcImage = "\uD83D\uDE31"))
        list.add(EmojiHome(emojiName = "Smirk", srcImage = "\uD83E\uDD73"))
        list.add(EmojiHome(emojiName = "Sunglasses", srcImage = "\uD83E\uDDD0"))
        list.add(EmojiHome(emojiName = "Tears of Joy", srcImage = "\uD83D\uDE44"))
        list.add(EmojiHome(emojiName = "Thinking", srcImage = "\uD83E\uDD7A"))
        list.add(EmojiHome(emojiName = "Very Angry", srcImage = "\uD83E\uDD71"))
        list.add(EmojiHome(emojiName = "Very Mad", srcImage = "\uD83E\uDD11"))
        list.add(EmojiHome(emojiName = "Very Sad", srcImage = "\uD83D\uDE32"))
        list.add(EmojiHome(emojiName = "Zipper Mouth", srcImage = "\uD83E\uDD2F"))

        return list
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }
}