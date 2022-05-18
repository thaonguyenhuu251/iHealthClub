package com.example.facebookclone.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.facebookclone.R
import com.example.facebookclone.model.User
import com.example.facebookclone.utils.KEY_USER
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_contact_email.*

class ContactEmailActivity : AppCompatActivity() {

    private var user : User? = null
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_email)

        Log.d("chContactEmailActivity", "onCreate: + ${intent.extras?.get(KEY_USER)}")
        user = intent.extras?.get(KEY_USER) as User

        btn_next.setOnClickListener {
            user?.email= te_email.text.toString().trim()
            val atemail = te_email.text.toString().trim()
            if(atemail.isNotEmpty()){
                val bundle = Bundle()
                bundle.putSerializable(KEY_USER,user)
                val i =Intent(this@ContactEmailActivity, OtpVerificationActivity::class.java)
                i.putExtras(bundle)
                startActivity(i)
            }else{
                Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show()
            }

        }

        tv_create.setOnClickListener {
            user?.email= te_email.text.toString().trim()
            val atemail = te_email.text.toString().trim()
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER,user)
            val j = Intent( this@ContactEmailActivity, ContactNumberActivity::class.java)
            j.putExtras(bundle)
            startActivity(j)
        }



        iv_back.setOnClickListener {
            finish()
        }
    }


    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
        // [END send_email_verification]
    }
}