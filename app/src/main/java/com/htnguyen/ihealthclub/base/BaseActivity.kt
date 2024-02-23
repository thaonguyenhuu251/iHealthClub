package com.htnguyen.ihealthclub.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htnguyen.ihealthclub.database.UserRepository
import com.htnguyen.ihealthclub.database.UserRoomDatabase
import com.htnguyen.ihealthclub.utils.PermissionHelper
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.view.dialog.LoadingDialog
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*


abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    abstract val layout: Int
    lateinit var binding: T
    abstract val viewModel: R
    var loadingDialog: LoadingDialog? = null
    var userRepository: UserRepository? = null
    lateinit var sharedPreferences: SharedPreferences

    companion object {
        var dLocale: Locale? = null
        const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 123
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
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        userRepository = UserRepository(UserRoomDatabase.getDatabase(this).userDao())
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

    fun checkPermissionImage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun showPermissionSettingsDialog() {
        /*val builder = AlertDialog.Builder(this, )

        val mLayoutInflater = LayoutInflater.from(this)
        val mView: View = mLayoutInflater.inflate(R.layout.dialog_alert, null)
        val message: TextView = mView.findViewById(com.htnguyen.noteplus.R.id.message)
        val description: TextView = mView.findViewById(com.htnguyen.noteplus.R.id.description)
        val positiveButton = mView.findViewById<Button>(com.htnguyen.noteplus.R.id.btn_positive)
        val negativeButton = mView.findViewById<Button>(com.htnguyen.noteplus.R.id.btn_negative)

        message.text = "Permission needed."
        description.text = "This permission is needed to read images. Please grant the permission in Settings. Go to Settings"
        builder.setCustomTitle(mView)

        val dialog = builder.show()
        positiveButton.setOnClickListener {
            goToSettingDevice()
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            dialog.dismiss()
        }*/
    }

    fun requestReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                ),
                REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }

    }

    private fun goToSettingDevice(){
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + this.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        this.startActivity(i)
    }

    fun showToast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

}