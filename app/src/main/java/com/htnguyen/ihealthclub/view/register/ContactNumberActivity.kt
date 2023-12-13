package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import com.htnguyen.ihealthclub.utils.KEY_VERIFIED_ID
import com.htnguyen.ihealthclub.utils.OTP_TIME_OUT
import com.htnguyen.ihealthclub.view.dialog.LoadingDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_contact_number.*
import java.util.concurrent.TimeUnit


class ContactNumberActivity : AppCompatActivity() {

    private var user: User? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var loadingDialog : LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_number)

        user = intent.extras?.get(KEY_USER) as User

        initFirebase()

        initView()

    }

    private fun initView() {
        loadingDialog = LoadingDialog(this);

        btn_next.setOnClickListener {
            loadingDialog?.showDialog()
            user?.phoneNumber = te_number.text.toString().trim()
            val atphoneNumber = te_number.text.toString().trim()
            if(atphoneNumber.isNotEmpty()){
                if (atphoneNumber.length > 10){
                    startPhoneNumberVerification(te_number.text.toString().trim())
                }else{
                    Toast.makeText(this,"Please enter valid phone number", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Please enter your phone number", Toast.LENGTH_SHORT).show()
            }


        }


        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun initFirebase() {
        auth = Firebase.auth
        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("hunghkp", "onVerificationCompleted: ")
                val code = p0.smsCode
                if (code != null) {
                    Log.d("hunghkp", "onVerificationCompleted: $code")
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("hunghkp", "onVerificationFailed: ")
                loadingDialog?.dismissDialog()
                Toast.makeText(this@ContactNumberActivity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                user?.phoneNumber = te_number.text.toString().trim()
                val bundle = Bundle()
                bundle.putSerializable(KEY_USER, user)
                bundle.putString(KEY_VERIFIED_ID,p0)

                val i = Intent(this@ContactNumberActivity, OtpVerificationActivity::class.java)
                i.putExtras(bundle)
                loadingDialog?.dismissDialog()
                startActivity(i)

            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                Log.d("hunghkp", "onCodeAutoRetrievalTimeOut: ")

            }

        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(OTP_TIME_OUT, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}