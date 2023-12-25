package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
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
import com.htnguyen.ihealthclub.BR
import com.htnguyen.ihealthclub.base.BaseActivity
import com.htnguyen.ihealthclub.databinding.ActivityContactNumberBinding
import com.htnguyen.ihealthclub.model.AreaCode
import com.htnguyen.ihealthclub.utils.*
import com.htnguyen.ihealthclub.view.adapter.CountryAdapter
import java.util.concurrent.TimeUnit
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactNumberActivity : BaseActivity<ActivityContactNumberBinding, RegisterViewModel>() {

    private var user: User? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    var popupWindow: PopupWindow? = null
    override val layout: Int
        get() = R.layout.activity_contact_number
    override val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.extras?.get(KEY_USER) as User

        initFirebase()
        initView()
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    private fun initView() {
        loadingDialog = LoadingDialog(this)

        binding.btnNext.setOnClickListener {
            loadingDialog?.showDialog()
            user?.phoneNumber = binding.teNumber.text.toString().trim()
            val atPhoneNumber = binding.teNumber.text.toString().trim()
            val atEmail = binding.teEmail.text.toString().trim()
            if (binding.tvBack.text == getString(R.string.email)) {
                if (atEmail.isNotEmpty() && checkEmail(atEmail)) {
                    user?.email = atEmail
                    val bundle = Bundle()
                    bundle.putSerializable(KEY_USER, user)
                    bundle.putString(TYPE_REGISTER, USER_EMAIL)
                    val i = Intent(this@ContactNumberActivity, ChoosePasswordActivity::class.java)
                    i.putExtras(bundle)
                    loadingDialog?.dismissDialog()
                    startActivity(i)
                } else if (atEmail.isEmpty()) {
                    loadingDialog?.dismissDialog()
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loadingDialog?.dismissDialog()
                    Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (atPhoneNumber.isNotEmpty()) {
                    if (atPhoneNumber.length > 10) {
                        startPhoneNumberVerification(binding.teNumber.text.toString().trim())
                    } else {
                        loadingDialog?.dismissDialog()
                        Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    loadingDialog?.dismissDialog()
                    Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        binding.imBack.setOnClickListener {
            finish()
        }

        binding.tvCreateEmail.setOnClickListener {
            binding.textMobileEmail.visibility = View.VISIBLE
            binding.textMobileNumber.visibility = View.GONE
            binding.tvCreateEmail.visibility = View.GONE
            binding.tvCreateNumber.visibility = View.VISIBLE
            binding.tvBack.text = getString(R.string.email)
            binding.tvHeaderMobile.text = getString(R.string.enter_your_email)
            binding.tvDescriptionMobile.text = getString(R.string.the_email)
            binding.sCountry.visibility = View.GONE
        }
        binding.tvCreateNumber.setOnClickListener {
            binding.textMobileNumber.visibility = View.VISIBLE
            binding.textMobileEmail.visibility = View.GONE
            binding.tvCreateNumber.visibility = View.GONE
            binding.tvCreateEmail.visibility = View.VISIBLE
            binding.tvBack.text = getString(R.string.mobile_number)
            binding.tvHeaderMobile.text = getString(R.string.enter_your_mobile_number)
            binding.tvDescriptionMobile.text = getString(R.string.the_mobile_number)
            binding.sCountry.visibility = View.VISIBLE
        }

        provideCountryPopupWindow(binding.sCountry)

    }

    private fun initFirebase() {
        auth = Firebase.auth
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                loadingDialog?.dismissDialog()
                Toast.makeText(this@ContactNumberActivity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                user?.phoneNumber = binding.teNumber.text.toString().trim()
                val bundle = Bundle()
                bundle.putSerializable(KEY_USER, user)
                bundle.putString(KEY_VERIFIED_ID, p0)
                bundle.putString(TYPE_REGISTER, USER_PHONE)

                val i = Intent(this@ContactNumberActivity, OtpVerificationActivity::class.java)
                i.putExtras(bundle)
                loadingDialog?.dismissDialog()
                startActivity(i)

            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
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

    private fun provideCountryPopupWindow(it: View) {
        val json = readJSONFromAsset(this@ContactNumberActivity, "area_code.json")
        val list = parseJsonToListAreaCode(json)
        val listAreaCode = ArrayList<AreaCode>()
        for (areaCode in list) {
            Log.d("ThaoNH", areaCode.toString())
            if (!areaCode.phone.isNullOrEmpty()) {
                for (phone in areaCode.phone) {
                    val listPhone = arrayListOf<String>(phone)
                    listAreaCode.add(
                        AreaCode(
                            areaCode.name,
                            areaCode.region,
                            listPhone.toList(),
                            areaCode.image,
                            areaCode.emoji
                        )
                    )
                }
            } else {
                listAreaCode.add(
                    AreaCode(
                        areaCode.name,
                        areaCode.region,
                        arrayListOf("0").toList(),
                        areaCode.image,
                        areaCode.emoji
                    )
                )
            }

        }
        val countryAdapter = CountryAdapter(this@ContactNumberActivity, list.toList())
        popupWindow = PopupWindow()
            .apply {
                val backgroundDrawable = ContextCompat.getDrawable(
                    this@ContactNumberActivity,
                    R.drawable.rounded_blue_outline_background_white
                )
                setBackgroundDrawable(backgroundDrawable)
                isOutsideTouchable = true
                val listView = layoutInflater.inflate(
                    R.layout.layout_area_code_dropdown,
                    null,
                    false
                ) as ListView
                binding.sCountry.adapter = countryAdapter
                listView.setOnItemClickListener { _, _, position, _ ->
                    val selectedCountry = countryAdapter.getItem(position)!!
                    popupWindow?.dismiss()
                }
                contentView = listView

                animationStyle = 0
                width = it.width
                height = 100
            }
    }
}