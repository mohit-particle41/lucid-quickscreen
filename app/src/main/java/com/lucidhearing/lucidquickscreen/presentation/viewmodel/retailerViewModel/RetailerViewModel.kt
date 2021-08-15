package com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetwork

class RetailerViewModel(
    private val app: Application
) : AndroidViewModel(app) {
    var retailerWifiNetworks: MutableList<WifiNetwork> = mutableListOf()
    var storeNumberList:MutableList<String> = mutableListOf()

    fun disconnectAllNetworks() {
        retailerWifiNetworks.forEach { network ->
            network.isConnected = false
        }
    }

    fun connectToNetwork(selectedWifiNetwork:WifiNetwork?){
        selectedWifiNetwork?.let{ wifiNetwork ->
            retailerWifiNetworks.forEach { network ->
                if (network.ssid == wifiNetwork.ssid && network.bssid == wifiNetwork.bssid) network.isConnected = true else false
            }
        }

    }
}