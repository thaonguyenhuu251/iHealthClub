package com.example.facebookclone.view.mainscreen.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
import com.example.facebookclone.utils.*
import com.example.facebookclone.view.adapter.EmojiHomeAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_emoji.*
import java.util.*
import kotlin.collections.ArrayList


const val KEY_EMOJI_PUT = "KEY_EMOJI_PUT"

class EmojiFragment : Fragment(),OnEmojiSearch {
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

    }

    private fun initRecycleView() {
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

    fun filter(text: String) {
        val filteredList: MutableList<EmojiHome> = mutableListOf<EmojiHome>()
        for (item in list) {
            if (item.emojiName.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        emojisAdapter.filterList(filteredList)
    }

    override fun onTextChance(search: String) {
        Log.d("nht", "onTextChanged: " + search)
        filter(search)
    }

}