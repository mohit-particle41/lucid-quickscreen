package com.lucidhearing.lucidquickscreen.presentation.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentConnectWifiNetworkDialogBinding
import com.lucidhearing.lucidquickscreen.databinding.FragmentDisconnectWifiNetworkDialogBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetwork
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.connectWifiNetworkViewModel.ConnectWifiNetworkViewModel

class DisconnectWifiNetworkDialogFragment : DialogFragment() {

    private lateinit var fragmentDisconnectWifiNetworkDialogBinding: FragmentDisconnectWifiNetworkDialogBinding
    private val connectWifiNetworkViewModel:ConnectWifiNetworkViewModel by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        fragmentDisconnectWifiNetworkDialogBinding = FragmentDisconnectWifiNetworkDialogBinding.inflate(
            inflater, container,false
        )
        val view  = fragmentDisconnectWifiNetworkDialogBinding.root

        connectWifiNetworkViewModel.selectedWifiNetwork?.let { network ->
            fragmentDisconnectWifiNetworkDialogBinding.apply {
                textviewNetworkName.text = getString(R.string.dialog_confirm_disconnect_wifi, network.ssid)
            }
        }

        fragmentDisconnectWifiNetworkDialogBinding.apply {
            buttonDismiss.setOnClickListener { dismiss() }
            buttonDisconnect.setOnClickListener {
                connectWifiNetworkViewModel.setDisconnectAction(true)
                dismiss()
            }
        }
        return view
    }

    companion object {
        const val TAG = "DisconnectWifiNetworkDialogFragment"
    }
}
