package com.htnguyen.ihealthclub.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.model.UserLogin
import com.htnguyen.ihealthclub.model.UserSaved
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.URL_PHOTO
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.utils.USER_NAME
import com.htnguyen.ihealthclub.view.login.LoginActivity
import com.htnguyen.ihealthclub.view.login.LoginProfileActivity
import com.htnguyen.ihealthclub.view.mainscreen.MainScreenActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private var userRepository: UserRepository? = null
    private var db: FirebaseFirestore? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        db = Firebase.firestore
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())

        if (userRepository?.allUsers!!.isEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(SplashScreenActivity@ this, LoginActivity::class.java))
                finish()
            }, 2000)
        } else {
            val users = userRepository?.allUsers!![0]
            db!!.collection("UserLogin").document(users.account).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject<UserLogin>()
                        if (user?.password == users.password) {
                            getProfileUser(user?.idUser, user?.account, user?.password)
                        } else {
                            showSnackBar("Phone or Password wrong, please check again !")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    showSnackBar("Username and password don't match, please try again")
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
                        val login =
                            Intent(this@SplashScreenActivity, MainScreenActivity::class.java)
                        startActivity(login)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(baseContext, exception.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}