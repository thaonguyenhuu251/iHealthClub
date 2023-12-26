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
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.login.LoginActivity
import com.htnguyen.ihealthclub.view.mainscreen.MainScreenActivity
import kotlinx.android.synthetic.main.activity_login.*

class SplashScreenActivity : AppCompatActivity() {
    private var userRepository: UserRepository? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())

        if (userRepository?.allUsers!!.isEmpty()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(SplashScreenActivity@ this, LoginActivity::class.java))
                finish()
            }, 2000)
        } else {
            val users = userRepository?.allUsers!![0]
            users.password?.let {
                FirebaseUtils.isUserLogin(
                    users.account,
                    it,
                    onSuccess = {userLogin ->
                        getProfileUser(userLogin.idUser, userLogin.account, userLogin.password)
                    },
                    onFailure = {
                        showSnackBar(it.message.toString())
                    })
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getProfileUser(idUser: String?, account: String?, password: String?) {
        if (idUser != null) {
            FirebaseUtils.getUserById(
                idUser,
                onSuccess = { user ->
                    val editor = sharedPreferences.edit()
                    editor.putString(USER_ID, idUser)
                    editor.putString(URL_PHOTO, user.photoUrl)
                    editor.putString(USER_NAME, user.name)
                    editor.apply()
                    editor.commit()
                    val login = Intent(this@SplashScreenActivity, MainScreenActivity::class.java)
                    startActivity(login)
                    finish()
                },
                onFailure = {exception ->

                    Toast.makeText(baseContext, exception.toString(), Toast.LENGTH_SHORT).show()
                }
            )

        }
    }
}