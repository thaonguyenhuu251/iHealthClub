package com.htnguyen.ihealthclub.base

import android.Manifest
import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htnguyen.ihealthclub.utils.PermissionHelper
import com.htnguyen.ihealthclub.view.dialog.LoadingDialog
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*


abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    abstract val layout: Int
    lateinit var binding: T
    abstract val viewModel: R
    var loadingDialog: LoadingDialog? = null

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
        loadingDialog = LoadingDialog(this)
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

    open fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun disableSoftInputFromAppearing(editText: EditText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            editText.setTextIsSelectable(true)
        } else {
            editText.setRawInputType(InputType.TYPE_NULL)
            editText.isFocusable = true
        }
    }

}