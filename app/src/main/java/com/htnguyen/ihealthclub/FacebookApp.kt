package com.htnguyen.ihealthclub

import android.app.Application
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.HashMap

/**
 * Created by Burhanuddin Rashid on 1/23/2018.
 */
class FacebookApp : Application() {
    val list = mutableListOf<TestTT>()

    companion object {
        val eventBus: PublishSubject<HashMap<String, Any>> by lazy { PublishSubject.create() }
        var photoApp: FacebookApp? = null
            private set
        private val TAG = FacebookApp::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        val config: EmojiCompat.Config
        config = BundledEmojiCompatConfig(applicationContext)
        EmojiCompat.init(config)
        photoApp = this
        initView()
    }

    private fun initView(){
        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }

}


