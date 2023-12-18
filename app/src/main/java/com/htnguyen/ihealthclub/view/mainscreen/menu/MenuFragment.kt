package com.htnguyen.ihealthclub.view.mainscreen.menu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.URL_PHOTO
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.utils.USER_NAME
import com.htnguyen.ihealthclub.view.login.LoginActivity
import com.htnguyen.ihealthclub.view.mainscreen.MainScreenActivity
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.img_avatar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var urlAvatar: String = ""
    private var userName: String = ""
    private var userRepository: UserRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        urlAvatar = sharedPreferences.getString(URL_PHOTO,"").toString()
        userName = sharedPreferences.getString(USER_NAME, "USER FACEBOOK").toString()
        userRepository = UserRepository(UserRoomDatabase.getDatabase(requireContext()).userDao())
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

        btn_log_out.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString(USER_ID, "")
            editor.putString(URL_PHOTO, "")
            editor.putString(USER_NAME, "")
            editor.apply()
            editor.commit()
            CoroutineScope(Dispatchers.IO).launch {
                userRepository?.deleteAll()
            }
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }


}