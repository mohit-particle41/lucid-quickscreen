package com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel

import androidx.lifecycle.ViewModel

class NotificationViewModel:ViewModel() {
    var networkStatus:Boolean? = null

    fun updateNetworkStatus(status:Boolean){
        networkStatus = status
    }
}