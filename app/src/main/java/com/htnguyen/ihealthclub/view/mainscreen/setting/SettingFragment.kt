package com.htnguyen.ihealthclub.view.mainscreen.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.base.BaseFragment
import com.htnguyen.ihealthclub.databinding.FragmentSettingBinding
import com.htnguyen.ihealthclub.utils.URL_PHOTO
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.utils.USER_NAME
import com.htnguyen.ihealthclub.view.login.LoginActivity
import com.htnguyen.ihealthclub.view.register.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : BaseFragment<FragmentSettingBinding, RegisterViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val layout: Int
        get() = R.layout.fragment_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView(){
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

        im_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }


}