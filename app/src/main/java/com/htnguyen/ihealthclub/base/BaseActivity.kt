package com.htnguyen.ihealthclub.base

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htnguyen.ihealthclub.utils.PermissionHelper
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    abstract val layout: Int
    lateinit var binding: T
    abstract val viewModel: R

    companion object {
        var dLocale: Locale? = null

    }

    var localeUpdatedContext: ContextWrapper? = null
    private var disposable: Disposable? = null

    override fun onConfigurationChanged(newConfig: Configuration) {
        newConfig.setLocale(dLocale)
        newConfig.setLayoutDirection(dLocale)
        super.onConfigurationChanged(newConfig)

    }

    private val permissionHelper: PermissionHelper by lazy {
        PermissionHelper()
    }
    var requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this@BaseActivity
        binding.setVariable(getBindingVariable(), viewModel)

    }


    abstract fun getBindingVariable(): Int

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkRecognition() {
        permissionHelper.withActivity(this)
            .check(Manifest.permission.ACTIVITY_RECOGNITION)
            .onSuccess {
                requestLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            }
            .onDenied {
                checkRecognition()
            }
            .onNeverAskAgain {

            }
            .run()
    }

    override fun onResume() {
        super.onResume()


    }


    override fun attachBaseContext(newBase: Context) {

    }

}