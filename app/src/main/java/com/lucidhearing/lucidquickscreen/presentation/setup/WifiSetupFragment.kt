package com.lucidhearing.lucidquickscreen.presentation.setup

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.SupplicantState
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentBeginSetupBinding
import com.lucidhearing.lucidquickscreen.databinding.FragmentWifiSetupBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetwork
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetworkAdapter
import com.lucidhearing.lucidquickscreen.presentation.util.ConnectWifiNetworkDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.DisconnectWifiNetworkDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.showAlert
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.connectWifiNetworkViewModel.ConnectWifiNetworkViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModel
import com.lucidhearing.lucidquickscreen.utils.AppUtils

class WifiSetupFragment : Fragment() {
    private lateinit var fragmentWifiSetupBinding: FragmentWifiSetupBinding
    private lateinit var wifiManager: WifiManager
    private lateinit var wifiNetworkAdapter: WifiNetworkAdapter
    private lateinit var retailerViewModel: RetailerViewModel
    private lateinit var connectWifiNetworkDialogFragment: ConnectWifiNetworkDialogFragment
    private lateinit var disconnectWifiNetworkDialogFragment: DisconnectWifiNetworkDialogFragment

    private val connectWifiNetworkViewModel: ConnectWifiNetworkViewModel by viewModels(ownerProducer = { this })

    val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { _intent ->
                val action = _intent.getAction();
                if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                    scanSuccess()
                }
            }
        }
    }

    val intentWifiResultReceiverSupplicant =
        IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
    val wifiResultReceiverSupplicant = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { _intent ->
                val action = _intent.getAction();
                if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                    val ssid = "\"${connectWifiNetworkViewModel.selectedWifiNetwork?.ssid}\""
                    if (wifiManager.connectionInfo.ssid.equals(ssid)) {
                        val supplicantState: SupplicantState? =
                            intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)
                        val supplicantStateError =
                            intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0)
                        if (supplicantStateError == WifiManager.ERROR_AUTHENTICATING) {
                            context?.unregisterReceiver(this)
                            connectWifiNetworkViewModel.connectionStatusMsg.postValue(getString(R.string.status_wifi_auth_failure))
                            Toast.makeText(
                                activity,
                                "Auth Error while connecting ${wifiManager.connectionInfo.ssid}",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }

                        if (supplicantState == SupplicantState.COMPLETED && wifiManager.connectionInfo.supplicantState.equals(SupplicantState.COMPLETED)) {
                            context?.unregisterReceiver(this)
                            connectWifiNetworkViewModel.connectionStatusMsg.postValue("")
                            retailerViewModel.connectToNetwork(connectWifiNetworkViewModel.selectedWifiNetwork)
                            updateNetworkList(retailerViewModel.retailerWifiNetworks)
                            showNetworkNotification(true)
                            Toast.makeText(
                                activity,
                                "CONNECTED to ${wifiManager.connectionInfo.ssid}",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wifi_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentWifiSetupBinding = FragmentWifiSetupBinding.bind(view)
        val setupActivity = (activity as SetupActivity)
        retailerViewModel = setupActivity.retailerViewModel
        wifiNetworkAdapter = setupActivity.wifiNetworkAdapter
        connectWifiNetworkDialogFragment = ConnectWifiNetworkDialogFragment()
        disconnectWifiNetworkDialogFragment = DisconnectWifiNetworkDialogFragment()

        addNextButtonClickListener()
        observeConnectionStatus()
        addWifiNetworkAdaptorListener()
        observeNetworkDialogAction()
    }


    override fun onResume() {
        super.onResume()
        val intentFilterScan = IntentFilter()
        intentFilterScan.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context?.registerReceiver(wifiScanReceiver, intentFilterScan)
        initWifiNetworkList()
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(wifiScanReceiver)
    }

    private fun initWifiNetworkList() {
        setWifiEnabled()
        fragmentWifiSetupBinding.rvWifiNetwork.apply {
            layoutManager = LinearLayoutManager(activity)
            if (retailerViewModel.retailerWifiNetworks.size > 0) {
                wifiNetworkAdapter.wifiNetworks = retailerViewModel.retailerWifiNetworks
                adapter = wifiNetworkAdapter
            } else {
                initWifiScanRequest()
            }
        }
    }

    private fun setWifiEnabled() {
        if (!wifiManager.isWifiEnabled) wifiManager.setWifiEnabled(true)
    }

    private fun addNextButtonClickListener() {
        fragmentWifiSetupBinding.buttonNext.setOnClickListener {
            if(AppUtils.isNetworkAvailable((activity as SetupActivity).applicationContext)){
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.wifiSetupFragment,true).build()
                findNavController().navigate(R.id.action_wifiSetupFragment_to_beginSetupFragment,null,navOptions)
            }else{
                showAlert(getString(R.string.alert_network_not_connected))
            }
        }
    }

    private fun checkFineLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }

    private fun addWifiNetworkAdaptorListener() {
        wifiNetworkAdapter.setOnItemClickListener { wifiNetwork ->
            if (wifiNetwork.isConnected) showDisconnectNetworkDialog(wifiNetwork)
            else showConnectNetworkDialog(wifiNetwork)
        }
    }

    private fun showConnectNetworkDialog(network: WifiNetwork) {
        connectWifiNetworkViewModel.selectedWifiNetwork = network
        connectWifiNetworkDialogFragment.show(
            childFragmentManager,
            ConnectWifiNetworkDialogFragment.TAG
        )
    }

    private fun showDisconnectNetworkDialog(network: WifiNetwork) {
        connectWifiNetworkViewModel.selectedWifiNetwork = network
        disconnectWifiNetworkDialogFragment.show(
            childFragmentManager,
            DisconnectWifiNetworkDialogFragment.TAG
        )
    }

    private fun observeConnectionStatus() {
        connectWifiNetworkViewModel.connectionStatusMsg.observeForever { status ->
            fragmentWifiSetupBinding.textviewConnectionStatus.text = status
        }
    }

    private fun observeNetworkDialogAction() {
        connectWifiNetworkViewModel.confirmAction.observe(viewLifecycleOwner, { state ->
            if (state) {
                connectToWifi(connectWifiNetworkViewModel.selectedWifiNetwork)
                connectWifiNetworkViewModel.setConfirmAction(false)
            }
        })

        connectWifiNetworkViewModel.disconnectAction.observe(viewLifecycleOwner, { state ->
            if (state) {
                connectWifiNetworkViewModel.apply {
                    selectedWifiNetwork?.let { network ->
                        disconnectWifi(network)
                    }
                    setDisconnectAction(false)
                }
            }
        })
    }

    private fun updateNetworkList(wifiNetworks: MutableList<WifiNetwork>) {
        wifiNetworkAdapter.wifiNetworks = wifiNetworks
        fragmentWifiSetupBinding.rvWifiNetwork.adapter = wifiNetworkAdapter
    }

    private fun initWifiScanRequest() {
        val success = wifiManager.startScan()
        if (!success) {
            scanFailure()
        }
    }

    private fun scanSuccess() {
        val connectionInfo = wifiManager.connectionInfo
        val results = wifiManager.scanResults
        val wifiNetworks: MutableList<WifiNetwork> = mutableListOf()
        results.forEach { scanResult ->
            val ssid = "\"${scanResult.SSID}\""
            val wifiNetwork =  WifiNetwork(
                ssid = scanResult.SSID,
                bssid = scanResult.BSSID,
                capabilities = scanResult.capabilities,
                isConnected = if (connectionInfo.ssid.equals(ssid)) true else false
            )
            if(wifiNetwork.ssid != "") { wifiNetworks.add(wifiNetwork) }
        }
        retailerViewModel.retailerWifiNetworks = wifiNetworks
        updateNetworkList(wifiNetworks)
    }

    private fun scanFailure() {
        Toast.makeText(
            activity,
            getString(R.string.error_unable_to_scan_wifi),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun connectToWifi(wifiNetwork: WifiNetwork?) {
        setWifiEnabled()
        wifiNetwork?.let { network ->
            val configuredNetworks = getConfiguredNetworks()
            configuredNetworks?.let { networks ->
                networks.forEach { network ->
                    wifiManager.removeNetwork(network.networkId)
                }
                retailerViewModel.disconnectAllNetworks()
                updateNetworkList(retailerViewModel.retailerWifiNetworks)
            }
            context?.registerReceiver(
                wifiResultReceiverSupplicant,
                intentWifiResultReceiverSupplicant
            )
            val wifiConfiguration = WifiConfiguration()
            wifiConfiguration.SSID = "\"${network.ssid}\""
            if (AppUtils.isNetworkSecured(network.capabilities)) {
                wifiConfiguration.preSharedKey = "\"${network.password}\""
            } else {
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            }
            val netId = wifiManager.addNetwork(wifiConfiguration)
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            val result = wifiManager.reconnect()
            val statusMsg =
                if (result) getString(R.string.status_wifi_connecting) else getString(R.string.status_wifi_unable_connect)
            connectWifiNetworkViewModel.connectionStatusMsg.postValue(statusMsg)
        }
    }

    private fun disconnectWifi(wifiNetwork: WifiNetwork) {
        setWifiEnabled()
        val configuredNetworks = getConfiguredNetworks()
        configuredNetworks?.let { networks ->
            networks.forEach { network ->
                if (network.SSID.equals("\"${wifiNetwork.ssid}\"")) {
                    wifiManager.removeNetwork(network.networkId)
                    retailerViewModel.disconnectAllNetworks()
                    updateNetworkList(retailerViewModel.retailerWifiNetworks)
                    showNetworkNotification(false)
                }
            }
        }
    }

    private fun getConfiguredNetworks(): List<WifiConfiguration>? {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        return wifiManager.configuredNetworks
    }

    private fun showNetworkNotification(state: Boolean) {
        (activity as SetupActivity).showNetworkNotification(state)
    }
}