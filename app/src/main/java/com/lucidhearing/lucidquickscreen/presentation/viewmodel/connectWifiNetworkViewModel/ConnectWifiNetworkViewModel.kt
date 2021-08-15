package com.lucidhearing.lucidquickscreen.presentation.viewmodel.connectWifiNetworkViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetwork

class ConnectWifiNetworkViewModel:ViewModel() {
    var selectedWifiNetwork:WifiNetwork? = null
    val confirmAction = MutableLiveData<Boolean>()
    var connectionStatusMsg = MutableLiveData<String>("")
    val disconnectAction = MutableLiveData<Boolean>()

    fun setConfirmAction(state: Boolean) {
        confirmAction.value = state
    }
    fun setDisconnectAction(state: Boolean) {
        disconnectAction.value = state
    }
}