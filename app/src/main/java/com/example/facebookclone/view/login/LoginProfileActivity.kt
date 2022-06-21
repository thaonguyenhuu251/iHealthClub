package com.example.facebookclone.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebookclone.R
import com.example.facebookclone.database.UserRepository
import com.example.facebookclone.database.UserRoomDatabase
import com.example.facebookclone.model.User
import com.example.facebookclone.model.UserSaved
import com.example.facebookclone.utils.*
import com.example.facebookclone.view.adapter.ProfileUserAdapter
import com.example.facebookclone.view.dialog.LoadingDialog
import com.example.facebookclone.view.mainscreen.MainScreenActivity
import com.example.facebookclone.view.register.JoinFacebookActivity
import com.example.facebookclone.view.register.WhatYourNameActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile_login.*

class LoginProfileActivity : AppCompatActivity() {

    private var profileAdapter: ProfileUserAdapter? = null
    private var listUser: List<UserSaved>? = null
    private var userRepository: UserRepository? = null
    private var db: FirebaseFirestore? = null
    private var loadingDialog: LoadingDialog? = null
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_login)
        loadingDialog = LoadingDialog(this)
        db = Firebase.firestore
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

        btn_create_account.setOnClickListener {
            val create = Intent(this, JoinFacebookActivity::class.java)
            startActivity(create)
        }

        ln_log_another_account.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }

        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())

        listUser = userRepository?.allUsers

        profileAdapter = ProfileUserAdapter(this, listUser = listUser) { userCast ->
            loadingDialog?.showDialog()
            db?.collection(COLLECTION_PATH_USER)!!.document(userCast.phoneNumber).get()

                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject<User>()
                        if (user?.password == userCast.password) {
                            //login
                            val editor = sharedPreferences.edit()
                            editor.putString(USER_ID, user?.phoneNumber)
                            editor.putString(URL_PHOTO, user?.photoUrl)
                            editor.putString(USER_NAME, user?.firstName +" " + user?.lastName)
                            editor.apply()
                            editor.commit()
                            val login = Intent(this, MainScreenActivity::class.java)
                            loadingDialog?.dismissDialog()
                            startActivity(login)
                            finish()
                        } else {
                            loadingDialog?.dismissDialog()
                            showSnackBar("Password wrong, please check again !")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    loadingDialog?.dismissDialog()
                    showSnackBar("Username and password don't match, please try again")
                }
        }


        rv_profile_login.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_profile_login.setHasFixedSize(true)
        rv_profile_login.adapter = profileAdapter
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }
}