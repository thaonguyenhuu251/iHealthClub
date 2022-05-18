package com.example.facebookclone.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.R
import com.example.facebookclone.model.User
import com.example.facebookclone.utils.*
import com.example.facebookclone.view.dialog.LoadingDialog
import com.example.facebookclone.view.forgotpassword.ForgotPasswordMobileActivity
import com.example.facebookclone.view.mainscreen.MainScreenActivity
import com.example.facebookclone.view.register.JoinFacebookActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.container

class LoginActivity : AppCompatActivity() {
    private var db: FirebaseFirestore? = null
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingDialog = LoadingDialog(this)
        db = Firebase.firestore
        initview()
    }

    private fun initview() {
        btn_create_account.setOnClickListener {
            val i = Intent(this, JoinFacebookActivity::class.java)
            startActivity(i)
        }

        tv_forgot_password.setOnClickListener {
            val j = Intent(this, ForgotPasswordMobileActivity::class.java)
            startActivity(j)
        }


        btn_login.setOnClickListener {
            if (et_phone_login.text.toString().trim()
                    .isNotEmpty() && et_password_login.text.toString().trim().isNotEmpty()
            ) {
                loadingDialog?.showDialog()
                var phoneNumber = et_phone_login.text.toString().trim()
                val password = et_password_login.text.toString().trim()
                if (phoneNumber.length >= 10) {
                    if (phoneNumber.startsWith("0")) {
                        phoneNumber = phoneNumber.replaceRange(0, 1, "+84")
                    }
                }
                db!!.collection(COLLECTION_PATH_USER).document(phoneNumber).get()

                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val user = document.toObject<User>()
                            if (user?.password == password) {
                                //login
                                val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString(USER_ID,user.phoneNumber)
                                editor.putString(URL_PHOTO,user.photoUrl)
                                editor.putString(USER_NAME, user.firstName + user.lastName)
                                val login = Intent(this, MainScreenActivity::class.java)
                                loadingDialog?.dismissDialog()
                                startActivity(login)
                                finish()
                            } else {
                                loadingDialog?.dismissDialog()
                                showSnackBar("Phone or Password wrong, please check again !")
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        loadingDialog?.dismissDialog()
                        showSnackBar("Username and password don't match, please try again")
                    }
            }

        }


    }

    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

}