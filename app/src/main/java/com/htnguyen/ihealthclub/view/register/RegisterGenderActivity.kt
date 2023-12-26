package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import kotlinx.android.synthetic.main.activity_register_your_gender.*

class RegisterGenderActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_your_gender)

        user = intent.extras?.get(KEY_USER) as User

        btn_next.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            val i = Intent(this@RegisterGenderActivity, RegisterContactActivity::class.java)
            i.putExtras(bundle)
            startActivity(i)
        }

        rb_female.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                rb_male.isChecked = false
                rb_custom.isChecked = false
                user?.gender = true
            }
        }

        rb_male.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                rb_female.isChecked = false
                rb_custom.isChecked = false
                user?.gender = false
            }
        }


        rb_custom.setOnCheckedChangeListener { compoundButton, checked ->
            if (checked) {
                rb_male.isChecked = false
                rb_female.isChecked = false
                user?.gender = null
            }
        }

        im_back.setOnClickListener {
            finish()
        }
    }
}