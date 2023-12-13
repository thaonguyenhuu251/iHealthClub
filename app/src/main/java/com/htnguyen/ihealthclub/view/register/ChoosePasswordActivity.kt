package com.htnguyen.ihealthclub.view.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.model.UserSaved
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.dialog.LoadingDialog
import com.htnguyen.ihealthclub.view.mainscreen.MainScreenActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_choose_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChoosePasswordActivity : AppCompatActivity() {

    private var user: User? = null
    val db = Firebase.firestore
    private var loadingDialog: LoadingDialog? = null
    private var userRepository: UserRepository? = null
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_password)
        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        loadingDialog = LoadingDialog(this)
        initview()


    }

    private fun initview() {
        Log.d("atvnummail", "onCreate: + ${intent.extras?.get(KEY_USER)}")
        user = intent.extras?.get(KEY_USER) as User
        te_password_confirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {

                val atpassword = te_password.text.toString().trim()
                val atConfirmPasword = te_password_confirm.text.toString().trim()
                if (atpassword.isNotEmpty() && atConfirmPasword.isNotEmpty() && editable.isNotEmpty()) {
                    if (!atConfirmPasword.equals(atpassword)) {
                        // give an error that password and confirm password not match
                        tv_status_password.text = "Password incorrect"
                        btn_login.isEnabled = false

                    } else {
                        tv_status_password.text = "Password match"
                        btn_login.isEnabled = true
                    }
                }


            }
        })



        btn_login.setOnClickListener {
            user?.password = te_password.text.toString().trim()
            if (te_password.text.toString().trim()
                    .isNotEmpty() && te_password_confirm.text.toString().trim().isNotEmpty()
            ) {
                loadingDialog?.showDialog()
                Log.d("hunghkp", "initview:  +$user")

                db.collection("users").document(user?.phoneNumber!!).set(user!!)
                    .addOnSuccessListener {
                        val editor = sharedPreferences.edit()
                        editor.putString(USER_ID,user?.phoneNumber)
                        editor.putString(URL_PHOTO,user?.photoUrl)
                        editor.putString(USER_NAME, user?.firstName +" " + user?.lastName)
                        editor.apply()
                        editor.commit()
                        val userSaved = UserSaved(phoneNumber = user!!.phoneNumber,
                            firstName = user!!.firstName,
                            lastName = user!!.lastName,
                            password = user!!.password,
                            photoUrl = user!!.photoUrl)
                        CoroutineScope(Dispatchers.IO).launch {
                            userRepository?.insert(userSaved)
                        }
                        val i =
                            Intent(this@ChoosePasswordActivity, MainScreenActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        loadingDialog?.dismissDialog()
                        startActivity(i)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        loadingDialog?.dismissDialog()
                        Toast.makeText(this,
                            "Create user error",
                            Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show()
            }

        }
        im_back.setOnClickListener {
            finish()
        }
    }
}