package com.lucidhearing.lucidquickscreen.utils.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log
import com.lucidhearing.lucidquickscreen.utils.AppUtils
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant

class USBConnectionReceiver : BroadcastReceiver() {

    private lateinit var usbConnectionReceiverListeners: USBConnectionReceiverListeners

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            val usbPermission =
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    AppUtils.UsbPermission.Granted else AppUtils.UsbPermission.Denied
            usbConnectionReceiverListeners.usbDeviceDetected(usbPermission)
        }
    }

    fun registerListener(receiver: USBConnectionReceiverListeners) {
        usbConnectionReceiverListeners = receiver
    }

    interface USBConnectionReceiverListeners {
        fun usbDeviceDetected(usbPermission: AppUtils.UsbPermission)
    }
}
