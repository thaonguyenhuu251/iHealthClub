package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chivorn.datetimeoptionspicker.DateTimePickerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import kotlinx.android.synthetic.main.activity_register_birthday.*
import kotlinx.android.synthetic.main.activity_register_birthday.timepicker
import java.util.*

class RegisterBirthdayActivity : AppCompatActivity() {

    private var pvTime: DateTimePickerView? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_birthday)

        user = intent.extras?.get(KEY_USER) as User

        initTimePicker()

        btn_next.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER, user)
            val i = Intent(this@RegisterBirthdayActivity, RegisterGenderActivity::class.java)
            i.putExtras(bundle)
            startActivity(i)
        }

        im_back.setOnClickListener {
            finish()
        }
    }

    private fun initTimePicker() {
        val selectedDate = Calendar.getInstance()
        val startDate = Calendar.getInstance()
        startDate[1900, 1] = 1
        val endDate = Calendar.getInstance()
        endDate[2022, 12] = 31
        pvTime = DateTimePickerView.Builder(
            this
        ) { date, v ->
            user?.birthDay = date.time
        }
            .setLayoutRes(
                R.layout.dialog_custom_time
            ) { v ->

            }
            .setType(booleanArrayOf(true, true, true, false, false, false))
            .setLabel("", "", "", "", "", "")
            .setDividerColor(Color.DKGRAY)
            .setContentSize(20)
            .setDate(selectedDate)
            .setRangDate(startDate, selectedDate)
            .setDecorView(timepicker)
            .setBackgroundId(0x00000000)
            .setOutSideCancelable(false)
            .build()
        pvTime?.setKeyBackCancelable(false)
        pvTime?.show()
    }
}