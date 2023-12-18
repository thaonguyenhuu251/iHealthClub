package com.htnguyen.ihealthclub.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.model.UserSaved
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.dialog.LoadingDialog
import com.htnguyen.ihealthclub.view.forgotpassword.ForgotPasswordMobileActivity
import com.htnguyen.ihealthclub.view.mainscreen.MainScreenActivity
import com.htnguyen.ihealthclub.view.register.JoinClubActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.model.UserLogin
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.container
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var db: FirebaseFirestore? = null
    private var loadingDialog: LoadingDialog? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var userRepository: UserRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingDialog = LoadingDialog(this)
        db = Firebase.firestore
        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

        initview()
    }

    private fun initview() {
        btn_create_account.setOnClickListener {
            val i = Intent(this, JoinClubActivity::class.java)
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
                db!!.collection("UserLogin").document(phoneNumber).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val user = document.toObject<UserLogin>()
                            if (user?.password == password ) {
                                getProfileUser(user.idUser, user.account, user.password)
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

    private fun getProfileUser(idUser: String?, account: String?, password: String?) {
        if (idUser != null) {
            db!!.collection("User").document(idUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        loadingDialog?.dismissDialog()
                        val userSaved = UserSaved(
                            account = account!!,
                            userName = user?.name,
                            password = password,
                            photoUrl = user?.photoUrl
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            userRepository?.insert(userSaved)
                        }
                        val editor = sharedPreferences.edit()
                        editor.putString(USER_ID, idUser)
                        editor.putString(URL_PHOTO, user?.photoUrl)
                        editor.putString(USER_NAME, user?.name)
                        editor.apply()
                        editor.commit()
                        val login = Intent(this@LoginActivity, MainScreenActivity::class.java)
                        startActivity(login)
                        finish()

                    }
                }
                .addOnFailureListener { exception ->
                    loadingDialog?.dismissDialog()
                    Toast.makeText(baseContext, exception.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}