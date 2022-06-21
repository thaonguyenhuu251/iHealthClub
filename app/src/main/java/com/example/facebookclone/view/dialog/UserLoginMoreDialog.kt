package com.example.facebookclone.view.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.example.facebookclone.R

class UserLoginMoreDialog(activity: Activity) {
    private var dialog: AlertDialog

    init {
        val view = activity.layoutInflater.inflate(R.layout.dialog_user_login_more, null)


        dialog = AlertDialog.Builder(activity,R.style.DialogWidth)
            .create().apply {
                setView(view)
                requestWindowFeature(Window.FEATURE_NO_TITLE)
//                window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCanceledOnTouchOutside(false)
            }
    }

    fun showDialog(){
        if (!dialog.isShowing){
            dialog.show()
        }
    }

    fun dismissDialog(){
        if (dialog.isShowing){
            dialog.dismiss()
        }
    }
}