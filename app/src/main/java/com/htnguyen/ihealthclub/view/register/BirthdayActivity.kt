package com.htnguyen.ihealthclub.view.register

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chivorn.datetimeoptionspicker.DateTimePickerView
import com.htnguyen.ihealthclub.R
import com.htnguyen.ihealthclub.model.User
import com.htnguyen.ihealthclub.utils.KEY_USER
import kotlinx.android.synthetic.main.activity_register_birthday.*
import java.text.SimpleDateFormat
import java.util.*

class BirthdayActivity : AppCompatActivity() {

    private var pvTime: DateTimePickerView? = null
    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_birthday)

        Log.d("atvname", "onCreate: + ${intent.extras?.get(KEY_USER)}")

        user = intent.extras?.get(KEY_USER) as User


        initTimePicker()

        btn_next.setOnClickListener {
            user?.birthday = "14/12/1997"
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER,user)
            val i = Intent(this@BirthdayActivity, WhatYourGenderActivity::class.java)
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
        ) { date, v ->Toast.makeText(this,getTime(date),Toast.LENGTH_SHORT).show()
        }
            .setLayoutRes(
                R.layout.datetimeoptionspicker_custom_time
            ) { v ->

            }
            .setType(booleanArrayOf(true, true, true, false, false, false))
            .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
            .setDividerColor(Color.DKGRAY)
            .setContentSize(20)
            .setDate(selectedDate)
            .setRangDate(startDate, selectedDate)
            .setDecorView(frame_picker_view) //非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
            .setBackgroundId(0x00000000)
            .setOutSideCancelable(false)
            .build()
        pvTime?.setKeyBackCancelable(false) //系统返回键监听屏蔽掉
        pvTime?.show()
    }

    private fun getTime(date: Date): String? { //可根据需要自行截取数据显示
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }
}