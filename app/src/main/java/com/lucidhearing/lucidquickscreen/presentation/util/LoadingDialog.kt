package com.lucidhearing.lucidquickscreen.presentation.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.lucidhearing.lucidquickscreen.R


class LoadingDialog {

    private var activity:Activity
    private var alertDialog: AlertDialog

    constructor(myActivity:Activity){
        activity = myActivity
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = activity.layoutInflater

        builder.setView(layoutInflater.inflate(R.layout.custom_loader,null))
        builder.setCancelable(true)

        alertDialog = builder.create()
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00DE4343")))
    }

    fun startLoadingDialog(){
        alertDialog.show()
    }

    fun dismissDialog(){
        if(alertDialog != null && alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }

}