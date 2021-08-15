package com.lucidhearing.lucidquickscreen.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import java.util.regex.Pattern

object AppUtils {
    fun isEmailValid(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})\$",
            Pattern.CASE_INSENSITIVE
        )
        return emailPattern.matcher(email).matches()
    }

    fun isZipValid(zip: String): Boolean {
        val zipPattern = Pattern.compile("^\\d{5}(?:[-\\s]\\d{4})?\$")
        return zipPattern.matcher(zip).matches()
    }

    fun isNameValid(name:String):Boolean{
        return if (name.length >= AppConstant.NAME_MIN_CHAR_LENGTH)  true else false
    }

    fun isPhoneValid(phone:String):Boolean{
        val phonePattern = Pattern.compile("^[0-9]{10}\$")
        return phonePattern.matcher(phone).matches()
    }

    enum class UsbPermission { Unknown, Requested, Granted, Denied }

    fun isNetworkAvailable(context: Context?):Boolean{
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        }
        else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
            return false
        }
        return false
    }

    fun isNetworkSecured(capabilities:String):Boolean{
        return capabilities.contains(AppConstant.SECURITY_CAPABILITY_WAP) || capabilities.contains(AppConstant.SECURITY_CAPABILITY_WEP)
    }

    fun isWifiPasswordValid(password:String):Boolean{
        return password.length >= AppConstant.WIFI_PASS_MIN_LENGTH
    }

    fun getNetworkSSID(ssid:String):String{
        return if(ssid == "") AppConstant.UNNAMED_NETWORK else ssid
    }
}