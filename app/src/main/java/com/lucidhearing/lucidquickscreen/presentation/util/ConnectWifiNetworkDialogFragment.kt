package com.lucidhearing.lucidquickscreen.presentation.util

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentConnectWifiNetworkDialogBinding
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.connectWifiNetworkViewModel.ConnectWifiNetworkViewModel
import com.lucidhearing.lucidquickscreen.utils.AppUtils

class ConnectWifiNetworkDialogFragment : DialogFragment() {

    private lateinit var fragmentConnectWifiNetworkDialogBinding:FragmentConnectWifiNetworkDialogBinding
    private val connectWifiNetworkViewModel:ConnectWifiNetworkViewModel by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        fragmentConnectWifiNetworkDialogBinding = FragmentConnectWifiNetworkDialogBinding.inflate(
            inflater, container,false
        )
        val view  = fragmentConnectWifiNetworkDialogBinding.root
        addPasswordFieldListener()
        connectWifiNetworkViewModel.selectedWifiNetwork?.let { network ->
            fragmentConnectWifiNetworkDialogBinding.apply {
                textviewNetworkName.text =  getString(R.string.dialog_confirm_connect_wifi, AppUtils.getNetworkSSID(network.ssid)) //network.ssid
                if (AppUtils.isNetworkSecured(network.capabilities)){
                    buttonConnect.isEnabled = false
                    textfieldPassword.visibility = View.VISIBLE
                } else{
                    buttonConnect.isEnabled = true
                    textfieldPassword.visibility = View.GONE
                }
            }
        }

        fragmentConnectWifiNetworkDialogBinding.apply {
            buttonDismiss.setOnClickListener { dismiss() }
            buttonConnect.setOnClickListener {
                connectWifiNetworkViewModel.selectedWifiNetwork?.password = textfieldPassword.editText?.text.toString()
                textfieldPassword.editText?.setText("")
                connectWifiNetworkViewModel.setConfirmAction(true)
                dismiss()
            }
        }
        return view
    }

    private fun addPasswordFieldListener(){
        fragmentConnectWifiNetworkDialogBinding.apply {
            textfieldPassword.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    buttonConnect.isEnabled = AppUtils.isWifiPasswordValid(s.toString())
                }
            })
        }
    }

    companion object {
        const val TAG = "ConnectWifiNetworkDialogFragment"
    }
}