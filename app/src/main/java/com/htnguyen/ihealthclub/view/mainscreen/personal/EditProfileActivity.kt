package com.htnguyen.ihealthclub.view.mainscreen.personal

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import com.chivorn.datetimeoptionspicker.DateTimePickerView
import com.chivorn.datetimeoptionspicker.OptionsPickerView
import com.htnguyen.ihealthclub.BR
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.base.BaseActivity
import com.htnguyen.ihealthclub.databinding.ActivityEditProfileBinding
import com.htnguyen.ihealthclub.utils.FirebaseUtils
import com.htnguyen.ihealthclub.utils.SHARED_PREFERENCES_KEY
import com.htnguyen.ihealthclub.utils.USER_ID
import com.htnguyen.ihealthclub.utils.Utils
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*


class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, PersonalProfileViewModel>() {
    override val layout: Int
        get() = R.layout.activity_edit_profile
    override val viewModel by viewModels<PersonalProfileViewModel>()

    var dtpvCustomTime: DateTimePickerView? = null
    var dtpvOptions: OptionsPickerView<String>? = null
    private val gender: ArrayList<String> = arrayListOf("Male", "Female", "Other")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edtBirthday.showSoftInputOnFocus = false
        edtBirthday.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val inputMethod: InputMethodManager =
                v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethod != null) {
                inputMethod.hideSoftInputFromWindow(v.windowToken, 0)
            }
            dtpvCustomTime?.show()
            true
        }

        edtGender.showSoftInputOnFocus = false
        edtGender.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val inputMethod: InputMethodManager =
                v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethod != null) {
                inputMethod.hideSoftInputFromWindow(v.windowToken, 0)
            }
            dtpvOptions?.show()
            true
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        im_back.setOnClickListener {
            finish()
        }

        btn_save.setOnClickListener {
            loadingDialog?.showDialog()
            FirebaseUtils.db.collection("User").document(viewModel.idUser.value.toString()).update(
                mapOf(
                    "name" to viewModel.userName.value.toString(),
                    "birthDay" to viewModel.userBirthDayLong.value,
                    "gender" to viewModel.userGender.value,
                    "height" to viewModel.userHeight.value?.toFloat(),
                    "weight" to viewModel.userWeight.value?.toFloat(),
                )
            ).addOnCompleteListener { updateData ->
                if (updateData.isSuccessful) {
                    loadingDialog?.dismissDialog()
                    finish()
                }
            }
        }

        initData()
        initCustomTimePicker()
        initCustomGender()
    }

    private fun initData() {
        viewModel.idUser.value =
            sharedPreferences.getString(USER_ID, "").toString()

        viewModel.getDataProfileUser()
    }

    private fun initCustomTimePicker() {
        val selectedDate: Calendar = Calendar.getInstance()
        selectedDate.timeInMillis = viewModel.userBirthDayLong.value!!
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(1900, 1, 1)
        val endDate: Calendar = Calendar.getInstance()
        endDate.set(Date().year - 1, 12, 30)
        dtpvCustomTime = DateTimePickerView.Builder(
            this@EditProfileActivity
        ) { date, v ->
            viewModel.userBirthDayLong.value = date.time
            edtBirthday.setText(Utils.convertDateToString(date))
        }.setType(booleanArrayOf(true, true, true, false, false, false))
            .setLabel("", "", "", "", "", "")
            .setDividerColor(Color.DKGRAY)
            .setContentSize(20)
            .setDate(selectedDate)
            .setRangDate(startDate, selectedDate)
            .setBackgroundId(0x00000000)
            .setOutSideCancelable(false)
            .setDate(selectedDate)
            .setRangDate(startDate, endDate)
            .setLayoutRes(R.layout.dialog_custom_time) { v ->
                v.findViewById<RelativeLayout>(R.id.confirm).visibility = View.VISIBLE
                v.findViewById<TextView>(R.id.tv_finish).setOnClickListener {
                    dtpvCustomTime?.returnData()
                    dtpvCustomTime?.dismiss()
                }
             }
            .setDividerColor(Color.BLACK)
            .build()
    }

    private fun initCustomGender() {
        dtpvOptions = OptionsPickerView.Builder(
            this
        ) { options1, options2, options3,  v ->
            if (gender[options1] == "Male") {
                viewModel.userGender.value = true
            } else if (gender[options1] == "Female") {
                viewModel.userGender.value = false
            } else {
                viewModel.userGender.value = null
            }
        }
            .setSubmitText("Sure")
            .setCancelText("Cancel")
            .setTitleText("Choose your gender")
            .setSubCalSize(12)
            .setTitleSize(18)
            .setTitleColor(Color.BLACK)
            .setSubmitColor(Color.BLUE)
            .setCancelColor(Color.BLUE)
            .setTitleBgColor(0x00000000) //night mode
            .setBgColor(0x00000000) //night mode
            .setContentTextSize(18)
            .setLinkage(false)
            .isCenterLabel(false)
            .setLabels("", "", "")
            .setCyclic(true, false, false)
            .setSelectOptions(0, 0, 0)
            .setOutSideCancelable(false)
            .build() as OptionsPickerView<String>?

        dtpvOptions?.setPicker(gender)
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }
}