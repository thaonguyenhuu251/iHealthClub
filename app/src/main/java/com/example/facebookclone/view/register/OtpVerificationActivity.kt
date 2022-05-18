package com.example.facebookclone.view.register

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.R
import com.example.facebookclone.model.User
import com.example.facebookclone.utils.KEY_USER
import com.example.facebookclone.utils.KEY_VERIFIED_ID
import com.example.facebookclone.utils.OTP_TIME_OUT
import com.example.facebookclone.view.dialog.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_otp_verification.*
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verifiedID: String? = null
    private var loadingDialog: LoadingDialog? = null
    private var timer : CountDownTimer ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        verifiedID = intent.getStringExtra(KEY_VERIFIED_ID)
        user = intent.extras?.get(KEY_USER) as User

        initFirebase()

        initView()


    }

    private fun initView() {
        tv_sendphone.text = user?.phoneNumber

        timer = object : CountDownTimer(120000, 1000) {
            override fun onTick(p0: Long) {
                tv_count_down.text = "OTP is invalid after ${p0 / 1000} seconds"
            }

            override fun onFinish() {
                tv_count_down.text = "OTP is invalid"
                btn_next.isEnabled = false
            }

        }
        timer?.start()

        loadingDialog = LoadingDialog(this)
        btn_next.setOnClickListener {
            loadingDialog?.showDialog()
            if (ed_otp.text.toString()
                    .isNotEmpty() && ed_otp.text.toString().length == 6 && verifiedID != null
            ) {
                auth.signInWithCredential(
                    PhoneAuthProvider.getCredential(
                        verifiedID!!,
                        ed_otp.text.toString()
                    )
                )
                    .addOnCompleteListener(
                        this
                    ) {
                        if (it.isSuccessful) {

                            val bundle = Bundle()
                            bundle.putSerializable(KEY_USER, user)
                            val i = Intent(this, ChoosePasswordActivity::class.java)
                            i.putExtras(bundle)
                            loadingDialog?.dismissDialog()
                            startActivity(i)
                        } else {
                            // Show Error
                            loadingDialog?.dismissDialog()
                            if (it.exception is FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                showSnackBar(it.exception?.message ?: "Verification Failed")
                            } else {
                                showSnackBar("Verification Failed")
                            }
                        }
                    }
            } else {
                showSnackBar("Please enter OTP to continue")
            }
        }
        ln_send_code_again.setOnClickListener {
            loadingDialog?.showDialog()
            startPhoneNumberVerification(user?.phoneNumber!!)
        }


        im_back.setOnClickListener {
            finish()
        }
    }


    private fun initFirebase() {
        auth = Firebase.auth
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
                Toast.makeText(this@OtpVerificationActivity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                btn_next.isEnabled = true
                timer?.start()
                loadingDialog?.dismissDialog()
                verifiedID = p0

//                user?.phoneNumber = te_number.text.toString().trim()
//                val bundle = Bundle()
//                bundle.putSerializable(KEY_USER, user)
//                bundle.putString(KEY_VERIFIED_ID, p0)
//
//                val i = Intent(this, OtpVerificationActivity::class.java)
//                i.putExtras(bundle)

//                startActivity(i)

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


    private fun showSnackBar(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }

}