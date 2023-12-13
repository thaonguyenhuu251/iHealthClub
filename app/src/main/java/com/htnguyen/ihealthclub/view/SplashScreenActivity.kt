package com.htnguyen.ihealthclub.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.view.login.LoginActivity
import com.htnguyen.ihealthclub.view.login.LoginProfileActivity

class SplashScreenActivity : AppCompatActivity() {
    private var userRepository : UserRepository ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())

        if (userRepository?.allUsers!!.isEmpty()){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(SplashScreenActivity@this, LoginActivity::class.java))
                finish()
            }, 2000)
            // dieu huong login

        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(SplashScreenActivity@this, LoginProfileActivity::class.java))
                finish()
            }, 2000)
            // dieu huong man chon user
        }


        // logic cua phan authoritor




    }
}