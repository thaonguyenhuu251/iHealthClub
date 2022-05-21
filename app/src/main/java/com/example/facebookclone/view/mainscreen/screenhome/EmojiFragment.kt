package com.example.facebookclone.view.mainscreen.screenhome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebookclone.R
import com.example.facebookclone.model.EmojiHome
import com.example.facebookclone.view.adapter.EmojiHomeAdapter
import kotlinx.android.synthetic.main.fragment_emoji.*


class EmojiFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var emojisAdapter: EmojiHomeAdapter? = null

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
        initView()
    }
    private fun initView(){
        initRecycleView()
    }

    private fun initRecycleView(){
        emojisAdapter = EmojiHomeAdapter(requireContext(), listEmojis = listEmojis()){emojisHome ->

        }

        rv_emoji.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        rv_emoji.setHasFixedSize(true)
        rv_emoji.adapter = emojisAdapter

    }

    private fun listEmojis() :  MutableList<EmojiHome>{
        val list = mutableListOf<EmojiHome>()
        list.add(EmojiHome(emojiName = "Big boy" , srcImage = R.drawable.img_options_reel))
        list.add(EmojiHome(emojiName = "Big boy" , srcImage = R.drawable.img_options_reel))
        list.add(EmojiHome(emojiName = "Big boy" , srcImage = R.drawable.img_options_reel))
        list.add(EmojiHome(emojiName = "Big boy" , srcImage = R.drawable.img_options_reel))
        return list
    }

}