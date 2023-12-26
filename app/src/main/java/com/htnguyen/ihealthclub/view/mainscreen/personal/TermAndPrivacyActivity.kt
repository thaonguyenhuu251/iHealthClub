package com.htnguyen.ihealthclub.view.mainscreen.personal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import kotlinx.android.synthetic.main.activity_terms_privacy.*

class TermAndPrivacyActivity : AppCompatActivity(){
    private var user : User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_privacy)

        Log.d("atvpassword", "onCreate: + ${intent.extras?.get(KEY_USER)}")
        user = intent.extras?.get(KEY_USER) as User

        btn_sign_up.setOnClickListener {

        }

        im_back.setOnClickListener {
            finish()
        }
    }
}