package com.example.facebookclone.view.mainscreen.menu

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.example.facebookclone.R
import com.example.facebookclone.utils.SHARED_PREFERENCES_KEY
import com.example.facebookclone.utils.URL_PHOTO
import com.example.facebookclone.utils.USER_NAME
import com.example.facebookclone.view.mainscreen.MainScreenActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.img_avatar


class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var urlAvatar: String = ""
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        urlAvatar = sharedPreferences.getString(URL_PHOTO,"").toString()
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView(){

        tv_user_name.text = userName

        Glide.with(this).load(sharedPreferences.getString(URL_PHOTO, ""))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_fb_avatar)).into(img_avatar)

        ln_user.setOnClickListener {
                (requireActivity() as MainScreenActivity).setCurrentFragment(2)
        }
    }


}