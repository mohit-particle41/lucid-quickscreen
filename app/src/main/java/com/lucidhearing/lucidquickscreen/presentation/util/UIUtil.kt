package com.lucidhearing.lucidquickscreen.presentation.util

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.lucidhearing.lucidquickscreen.R

fun Fragment.showErrorSnackBar(message:String,view:View){
    val snackbar = Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).setAction("Dismiss", {})
    snackbar.setTextColor(Color.RED)
    snackbar.show()
}

fun String.toBoolean() = equals("YES", ignoreCase = true)

fun String.parsePhoneNumber():String{
    val phoneNoDash = this.trim().replace("-","")
    val phoneNoOpenBrackets = phoneNoDash.replace("(","")
    val phoneNoCloseBrackets = phoneNoOpenBrackets.replace(")","")
    val phone = phoneNoCloseBrackets.replace(" ","")
    return phone
}

fun AppCompatActivity.showNetworkConnectionNotification(state:Boolean, parentView: View){
    var messageResource = if (state) R.string.notification_network_connected else R.string.notification_no_internet
    var bgColorResource = if(state) R.color.lq_success_notification else R.color.lq_error_notification

    val snackbar = Snackbar.make(parentView, resources.getString(messageResource), Snackbar.LENGTH_LONG)
    snackbar.setAction(resources.getString(R.string.button_dismiss), {})
    val view = snackbar.getView();
    val param = view.layoutParams as CoordinatorLayout.LayoutParams
    param.gravity = Gravity.CENTER_HORIZONTAL
    view.layoutParams = param
    snackbar.setBackgroundTint(ContextCompat.getColor(this,bgColorResource))
    snackbar.setTextColor(Color.WHITE)
    snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.lq_snackbar_action))
    snackbar.show()
}

fun Fragment.showAlert(message: String) {
    val alertDialog = AlertDialogFragment()
    alertDialog.message = message
    alertDialog.show(childFragmentManager, AlertDialogFragment.TAG)
}