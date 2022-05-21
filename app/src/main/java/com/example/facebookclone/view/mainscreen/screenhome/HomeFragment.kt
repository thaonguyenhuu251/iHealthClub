package com.example.facebookclone.view.mainscreen.screenhome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.facebookclone.R
import com.example.facebookclone.model.OptionsHome
import com.example.facebookclone.utils.SHARED_PREFERENCES_KEY
import com.example.facebookclone.utils.URL_PHOTO
import com.example.facebookclone.view.adapter.OptionsHomeAdapter
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.img_avatar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var optionsAdapter: OptionsHomeAdapter? =null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        initView()

    }

    private fun initView(){
        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_fb_avatar)).into(img_avatar)
        tv_thinking_home.setOnClickListener {
            val intent = Intent(this@HomeFragment.context, CreatePostsActivity::class.java)
            startActivity(intent)
        }
        initRecyclerView()
    }

    private fun initRecyclerView(){
        optionsAdapter = OptionsHomeAdapter(requireContext(), listOptions = listOption()){optionsHome ->

        }

        rv_home.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        rv_home.setHasFixedSize(true)
        rv_home.adapter = optionsAdapter
    }

    private fun listOption() :  MutableList<OptionsHome>{
        val list = mutableListOf<OptionsHome>()
        list.add(OptionsHome(optionName = "Reels" , srcImage = R.drawable.img_options_reel, backgroundColor = R.color.color_reel ))
        list.add(OptionsHome(optionName = "Room" , srcImage = R.drawable.img_options_room, backgroundColor = R.color.color_room ))
        list.add(OptionsHome(optionName = "Group" , srcImage = R.drawable.img_options_group, backgroundColor = R.color.color_group ))
        list.add(OptionsHome(optionName = "Live" , srcImage = R.drawable.img_options_live, backgroundColor = R.color.color_live ))
        return list
    }


}