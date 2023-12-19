package com.htnguyen.ihealthclub

import android.app.Application
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.htnguyen.ihealthclub.di.viewModelModule
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.HashMap

class ClubApp : Application() {

    companion object {
        val eventBus: PublishSubject<HashMap<String, Any>> by lazy { PublishSubject.create() }
        var photoApp: ClubApp? = null
            private set
        private val TAG = ClubApp::class.java.simpleName
        lateinit var mInstance: ClubApp
    }

    override fun onCreate() {
        super.onCreate()
        val config: EmojiCompat.Config
        config = BundledEmojiCompatConfig(applicationContext)
        EmojiCompat.init(config)
        photoApp = this
        initView()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ClubApp)

            modules(
                listOf(viewModelModule)
            )
        }
        mInstance = this
    }

    private fun initView(){
        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }

}


