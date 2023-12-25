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
import com.google.firebase.auth.FirebaseAuth
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
import com.htnguyen.ihealthclub.model.UserLogin
import kotlinx.android.synthetic.main.activity_choose_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class ChoosePasswordActivity : AppCompatActivity() {

    private var user: User? = null
    private var loadingDialog: LoadingDialog? = null
    private var userRepository: UserRepository? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_password)
        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        loadingDialog = LoadingDialog(this)
        initView()
    }

    private fun initView() {
        user = intent.extras?.get(KEY_USER) as User
        te_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val atPassword = te_password.text.toString().trim()
                val atConfirmPassword = te_password_confirm.text.toString().trim()
                if (atPassword.isNotEmpty() && atConfirmPassword.isNotEmpty()) {
                    if (atConfirmPassword != atPassword) {
                        tv_status_password.text = "Password incorrect"
                        btn_login.isEnabled = false

                    } else {
                        tv_status_password.text = "Password match"
                        btn_login.isEnabled = true
                    }
                }
            }
        })
        te_password_confirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {

                val atPassword = te_password.text.toString().trim()
                val atConfirmPassword = te_password_confirm.text.toString().trim()
                if (atPassword.isNotEmpty() && atConfirmPassword.isNotEmpty() && editable.isNotEmpty()) {
                    if (!atConfirmPassword.equals(atPassword)) {
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
            if (te_password.text.toString().trim()
                    .isNotEmpty() && te_password_confirm.text.toString().trim().isNotEmpty()
            ) {
                loadingDialog?.showDialog()
                val userLogin = UserLogin(
                    idUser = user?.idUser,
                    account = if (intent.extras?.getString(TYPE_REGISTER) == USER_EMAIL) user?.email!! else user?.phoneNumber,
                    password = te_password.text.toString().trim()
                )

                if (intent.extras?.getString(TYPE_REGISTER) == USER_EMAIL) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(userLogin.account!!, userLogin.password!!)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                userLogin.idUser = task.result.user?.uid
                                createAccount(userLogin)
                            } else {
                                loadingDialog?.dismissDialog()
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    createAccount(userLogin)
                }
            } else {
                Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show()
            }

        }
        im_back.setOnClickListener {
            finish()
        }
    }

    private fun createAccount(userLogin: UserLogin) {
        FirebaseUtils.db.collection("UserLogin").document(userLogin.account!!).set(userLogin)
            .addOnSuccessListener {
                val editor = sharedPreferences.edit()
                editor.putString(USER_ID, userLogin.idUser)
                editor.putString(URL_PHOTO, user?.photoUrl)
                editor.putString(USER_NAME, user?.name)
                editor.apply()
                editor.commit()
                val userSaved = UserSaved(
                    account = userLogin.account!!,
                    userName = user?.name,
                    password = userLogin.password,
                    photoUrl = user?.photoUrl
                )
                CoroutineScope(Dispatchers.IO).launch {
                    userRepository?.insert(userSaved)
                }

                FirebaseUtils.db.collection("User").document(userLogin.idUser!!).set(user!!)
                    .addOnSuccessListener {
                        loadingDialog?.dismissDialog()
                        val i = Intent(this@ChoosePasswordActivity, MainScreenActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        loadingDialog?.dismissDialog()
                        startActivity(i)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        loadingDialog?.dismissDialog()
                        finish()
                    }

            }
            .addOnFailureListener { e ->
                loadingDialog?.dismissDialog()
                Toast.makeText(
                    this,
                    "Create user error",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}