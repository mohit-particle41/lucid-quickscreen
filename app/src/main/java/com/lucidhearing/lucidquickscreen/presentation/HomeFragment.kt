package com.lucidhearing.lucidquickscreen.presentation

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.util.SerialInputOutputManager
import com.lucid.OAE.OAETestProvider
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentHomeBinding
import com.lucidhearing.lucidquickscreen.presentation.setup.SetupActivity
import com.lucidhearing.lucidquickscreen.presentation.util.LoadingDialog
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.utils.AppUtils
import com.lucidhearing.lucidquickscreen.utils.TextUtil
import com.lucidhearing.lucidquickscreen.utils.broadcastReceiver.USBConnectionReceiver
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import com.lucidhearing.lucidquickscreen.utils.usbSerialService.SerialService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    lateinit var customerViewModel: CustomerViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var loadingDialog:LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val homeActivity = (activity as HomeActivity)
        loadingDialog = LoadingDialog(homeActivity)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = FragmentHomeBinding.bind(view)
        customerViewModel = (activity as HomeActivity).customerViewModel
        fragmentHomeBinding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_customerLogin)
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img_banner1,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img_banner2,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img_banner3,ScaleTypes.FIT))

        if (fragmentHomeBinding.imageSlider != null) {
            fragmentHomeBinding.imageSlider!!.setImageList(imageList)
        }

        fragmentHomeBinding.buttonSetup?.setOnClickListener {
            //(activity as HomeActivity).getEsperDeviceInfo()
            val setupActivityIntent = Intent((activity as HomeActivity),SetupActivity::class.java)
            startActivity(setupActivityIntent)
        }

        fragmentHomeBinding.buttonTutorial?.setOnClickListener {
            lifecycleScope.launch {
                val homeActivity = (activity as HomeActivity)
                val apiKey = homeActivity.retailerPreferences.retailerAPIKey.first()
                if(apiKey == null){
                    homeActivity.retailerPreferences.saveRetailerAPIKey("--my API Key--")
                }else{
                    homeActivity.retailerPreferences.removeRetailerAPIKey()
                }
            }
            (activity as HomeActivity).getCustomConfig()
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
