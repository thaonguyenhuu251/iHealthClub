package com.example.facebookclone

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory

/**
 * Created by Burhanuddin Rashid on 1/23/2018.
 */
class FacebookApp : Application() {
    val list = mutableListOf<TestTT>()
    val listPro = mutableListOf<TestTT>()

    override fun onCreate() {
        super.onCreate()
        val config: EmojiCompat.Config
        config = BundledEmojiCompatConfig(applicationContext)
        EmojiCompat.init(config)
        photoApp = this
        testt()
        addtest()
        Log.d("nht", listPro.toString())
        initView()
    }

    private fun initView(){
        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }


    companion object {

        var photoApp: FacebookApp? = null
            private set
        private val TAG = FacebookApp::class.java.simpleName
    }

    fun testt() {
        list.add(TestTT(1, "A"))
        list.add(TestTT(1, "B"))
        list.add(TestTT(1, "C"))
        list.add(TestTT(2, "A"))
        list.add(TestTT(2, "B"))
        list.add(TestTT(2, "C"))
        list.add(TestTT(2, "D"))
        list.add(TestTT(3, "A"))
        list.add(TestTT(3, "B"))
        list.add(TestTT(4, "B"))
        list.add(TestTT(4, "D"))
    }

    fun check(a: Int, b: MutableList<TestTT>): Int {
        for(i in b){
             if (a == i.id)
                 return 0
        }
        return 1
    }

    fun addtest() {
        if (list.isNullOrEmpty()) {

        } else {
            for(i in list){
                if(listPro.isNullOrEmpty()){
                    listPro.add(TestTT(i.id, i.name))
                }else{
                    if(check(i.id,listPro)==1)
                        listPro.add((TestTT(i.id,i.name)))
                }
            }

        }

    }


}


