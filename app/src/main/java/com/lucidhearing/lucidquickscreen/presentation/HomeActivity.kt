package com.lucidhearing.lucidquickscreen.presentation

import android.app.PendingIntent
import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.Entry
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import com.lucid.OAE.*
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.prefDataStore.RetailerPreferences
import com.lucidhearing.lucidquickscreen.databinding.ActivityMainBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.LifestyleQuestionAdapter
import com.lucidhearing.lucidquickscreen.presentation.adapter.ProductRecommendationAdapter
import com.lucidhearing.lucidquickscreen.presentation.util.showNetworkConnectionNotification
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel.HearingTestViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel.HearingTestViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.lifestyleQuestionViewModel.LifestyleQuestionViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.lifestyleQuestionViewModel.LifestyleQuestionViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel.NotificationViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel.NotificationViewModelFactory
import com.lucidhearing.lucidquickscreen.utils.AppUtils
import com.lucidhearing.lucidquickscreen.utils.HearingTestUtil
import com.lucidhearing.lucidquickscreen.utils.NetworkConnection
import com.lucidhearing.lucidquickscreen.utils.broadcastReceiver.USBConnectionReceiver
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import com.lucidhearing.lucidquickscreen.utils.syncService.CustomerDataSyncService
import com.lucidhearing.lucidquickscreen.utils.usbSerialService.SerialListener
import com.lucidhearing.lucidquickscreen.utils.usbSerialService.SerialService
import com.lucidhearing.lucidquickscreen.utils.TextUtil
import com.lucidhearing.lucidquickscreen.utils.usbSerialService.SerialSocket
import dagger.hilt.android.AndroidEntryPoint
import io.esper.devicesdk.EsperDeviceSDK
import io.esper.devicesdk.models.EsperDeviceInfo
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(),
    IOAETestReceiver,
    USBConnectionReceiver.USBConnectionReceiverListeners, SerialListener {

    @Inject
    lateinit var customerViewModelFactory: CustomerViewModelFactory

    @Inject
    lateinit var lifestyleQuestionViewModelFactory: LifestyleQuestionViewModelFactory

    @Inject
    lateinit var hearingTestViewModelFactory: HearingTestViewModelFactory

    @Inject
    lateinit var notificationViewModelFactory: NotificationViewModelFactory

    @Inject
    lateinit var customerDataSyncService: CustomerDataSyncService

    @Inject
    lateinit var lifestyleQuestionAdapter: LifestyleQuestionAdapter

    @Inject
    lateinit var productRecommendationAdapter: ProductRecommendationAdapter

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var retailerPreferences: RetailerPreferences

    private enum class Connected {
        False, Pending, True
    }

    private lateinit var activityMainBinding: ActivityMainBinding
    lateinit var customerViewModel: CustomerViewModel
    lateinit var lifestyleQuestionViewModel: LifestyleQuestionViewModel
    lateinit var hearingTestViewModel: HearingTestViewModel
    lateinit var notificationViewModel: NotificationViewModel

    lateinit var sdk: EsperDeviceSDK

    lateinit var usbConnectionReceiver: USBConnectionReceiver
    lateinit var mTestProvider: OAETestProvider
    var usbPermission: AppUtils.UsbPermission = AppUtils.UsbPermission.Unknown
    var usbSerialPort: UsbSerialPort? = null
    private val ACTION_USB_PERMISSION = AppConstant.INTENT_ACTION_GRANT_USB
    var service: SerialService? = null
    private var connected = Connected.False

    private var isUsbOnAttach = false
    private var isConfigChange = false

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        Log.i(AppConstant.DEBUG_TAG, "Permission granted for device $device")
                        connect(true)
                    }
                }
            }
        }
    }

    private val usbDetachReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                synchronized(this) {
                    Log.i(AppConstant.DEBUG_TAG, "USB device detached...")
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                }
            }
        }
    }

    val restrictionsFilter = IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED)
    val restrictionsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var myRestrictionsMgr =
                getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
            val appRestrictions = myRestrictionsMgr.applicationRestrictions
            if (appRestrictions.containsKey("Retailer")) {
                Toast.makeText(applicationContext,
                    "Retailer from Managed Config-" + appRestrictions.getString("Retailer"),
                    Toast.LENGTH_LONG ).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        customerViewModel = ViewModelProvider(this, customerViewModelFactory)
            .get(CustomerViewModel::class.java)
        lifestyleQuestionViewModel = ViewModelProvider(this, lifestyleQuestionViewModelFactory)
            .get(LifestyleQuestionViewModel::class.java)
        hearingTestViewModel = ViewModelProvider(this, hearingTestViewModelFactory)
            .get(HearingTestViewModel::class.java)
        notificationViewModel = ViewModelProvider(this, notificationViewModelFactory)
            .get(NotificationViewModel::class.java)
        usbConnectionReceiver = USBConnectionReceiver()
        usbConnectionReceiver.registerListener(this as USBConnectionReceiver.USBConnectionReceiverListeners)
        mTestProvider = OAETestProvider(this)
        observeNetworkState()

        hearingTestViewModel.scannerConnectionStatus.observe(this, { status ->
            val resourceID =
                if (status) R.drawable.ic_device_connected else R.drawable.ic_device_not_connected
            activityMainBinding.imgDeviceConnected.setImageResource(resourceID)
        })

        hearingTestViewModel.getResultActionState.observe(this, { state ->
            if (state) {
                getResult()
            }
        })
        if (savedInstanceState != null) {
            isConfigChange = savedInstanceState.get("isConfigChange") as Boolean
        }
        observerBinder()
        checkEsperSDKActivation()
    }

    fun checkEsperSDKActivation(){
        sdk = EsperDeviceSDK.getInstance(applicationContext)
        lifecycleScope.launch {
            sdk.isActivated(object : EsperDeviceSDK.Callback<Boolean> {
                override fun onResponse(active: Boolean?) {
                    active?.let { state ->
                        if(!state){
                            esperConfig()
                        }
                    }
                }
                override fun onFailure(t: Throwable) {
                    Log.i("TestTag", "Error while checking activation - " + t.stackTraceToString())
                }
            })
        }
    }

    fun esperConfig() {
        sdk.activateSDK(AppConstant.ESPER_SDK_KEY, object : EsperDeviceSDK.Callback<Void?> {
            override fun onResponse(response: Void?) {
                Toast.makeText(applicationContext, "Esper SDK Activated", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(applicationContext, "Esper Activation Failed", Toast.LENGTH_LONG)
                    .show()
                Log.i("TestTag", "Esper Activation failed->" + t.stackTraceToString())
            }
        })
    }

    fun getEsperDeviceInfo(){
        sdk.getEsperDeviceInfo(object:EsperDeviceSDK.Callback<EsperDeviceInfo>{
            override fun onResponse(esperDeviceInfo: EsperDeviceInfo?) {
                esperDeviceInfo?.let { info ->
                    Toast.makeText(applicationContext,
                        "Esper Device Info: Device ID - ${info.deviceId}  Serial No - ${info.serialNo}",
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(t: Throwable) {
                Log.i("TestTag", "Error while fetching esper device info - " + t.stackTraceToString())
            }
        })
    }

    fun getCustomConfig(){
        var myRestrictionsMgr =
            getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
        val appRestrictions = myRestrictionsMgr.applicationRestrictions
        if (appRestrictions.containsKey("Retailer")) {
            Toast.makeText(applicationContext,
                "Retailer from Managed Config-" + appRestrictions.getString("Retailer"),
                Toast.LENGTH_LONG ).show()
        }
    }

    private fun observerBinder() {
        hearingTestViewModel.getBinder().observe(this, { serviceBinder ->
            if (serviceBinder != null) {
                service = serviceBinder.service
                service?.attach(this)
                if (hearingTestViewModel.firstRun && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    hearingTestViewModel.firstRun = false
                    runOnUiThread(Runnable { this.connect() })
                }
            }
        })
    }

    override fun onDestroy() {
        try {
            unbindService(hearingTestViewModel.getServiceConnection())
        } catch (ignored: java.lang.Exception) {
        }
        if (!isChangingConfigurations) {
            if (hearingTestViewModel.isDeviceConnected != false) disconnect()
            stopService(Intent(this, SerialService::class.java))
        }
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        if (service != null) {
            service!!.attach(this)
        } else {
            if (!isConfigChange) {
                startService(Intent(this, SerialService::class.java))
            }
            bindService(
                Intent(this, SerialService::class.java),
                hearingTestViewModel.getServiceConnection(),
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        if (service != null && !isChangingConfigurations) {
            service!!.detach()
        }
        super.onStop()
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(usbConnectionReceiver)
        unregisterReceiver(usbDetachReceiver)
        unregisterReceiver(usbReceiver)
        unregisterReceiver(restrictionsReceiver)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(usbConnectionReceiver, IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED))
        registerReceiver(usbDetachReceiver, IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED))
        registerReceiver(restrictionsReceiver, restrictionsFilter)
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(usbReceiver, filter)
        if ((hearingTestViewModel.firstRun && service != null || isUsbOnAttach)) {
            hearingTestViewModel.firstRun = false
            isUsbOnAttach = false
            runOnUiThread(Runnable { this.connect() })
        }
    }

    private fun observeNetworkState() {
        networkConnection.observe(this, Observer { isConnected ->
            if (notificationViewModel.networkStatus != isConnected) {
                notificationViewModel.updateNetworkStatus(isConnected)
                showNetworkNotification(isConnected)
                if (isConnected)
                    customerDataSyncService.checkOrCreateDataSyncWorkRequest()
                else
                    customerDataSyncService.cancelAllWorkRequest()
            }
        })
    }

    private fun showNetworkNotification(state: Boolean) {
        showNetworkConnectionNotification(state, activityMainBinding.homeCoordinatorLayout)
    }

    //IOAETestsListener
    override fun send(data: ByteArray?) {
        if (service != null) {
            service!!.write(data)
        }
    }

    override fun OnBulkTestResultsReady(testResults: ArrayList<OAETest>?) {
        testResults?.let { resultList ->
//            Use for bulk request
//            hearingTestViewModel.setHearingTestRawResult(resultList)
//            val resultMap = HearingTestUtil.prepareGraphData(resultList)
//            hearingTestViewModel.setReadingTestData(resultMap)
        }
    }

    override fun OnRealTestDisplayInit(testDisplayData: TestDisplayData?) {
        testDisplayData?.let{ displayData ->
            val testDisplayData = displayData.ear
            hearingTestViewModel.resetTestData(displayData.ear.toString())
            hearingTestViewModel.scanningStatus.postValue(true)
            hearingTestViewModel.scanStatusMsg.postValue(getString(R.string.scan_status_progress))
        }
    }

    override fun OnRealTimeTestResultReady(dpTestFrequencyDisplay: DPTestFrequencyDisplay?) {
        if (!hearingTestViewModel.shouldReadTestResult) return
        dpTestFrequencyDisplay?.let { dpFreqData ->
            hearingTestViewModel.addReadingToResult(HearingTestUtil.readFrequencyFromScanner(dpFreqData))
        }
    }

    override fun DisplayMessage(message: String?) {
        Log.i(AppConstant.DEBUG_TAG, "${message}")
    }

    override fun OnPollDetected(PollDetected: Boolean) {}

    override fun isBLE(): Boolean {
        return false
    }

    override fun usbDeviceDetected(usbPermission: AppUtils.UsbPermission) {
        this.usbPermission = usbPermission
        connect()
    }

    private fun getResult() {
        if (service != null && hearingTestViewModel.isDeviceConnected) {
            mTestProvider.GetTestResults()
        }
        hearingTestViewModel.getResultActionState.postValue(false)
    }

    //Serial Listener
    override fun onSerialConnect() {
        connected = Connected.True
        hearingTestViewModel.isDeviceConnected = true
    }

    override fun onSerialConnectError(e: Exception?) {
        Log.i(
            AppConstant.DEBUG_TAG,
            "onSerialConnectError - " + e?.message.toString()
        )
        disconnect()
    }

    override fun onSerialRead(data: ByteArray?) {
        data?.let {
            receive(data)
        }
    }

    override fun onSerialIoError(e: Exception?) {
        Log.i(AppConstant.DEBUG_TAG, "onSerialIoError - " + e?.message.toString())
        disconnect()
    }

    private fun receive(data: ByteArray) {
        var msg = String(data)
        Log.i(AppConstant.DEBUG_TAG, "onSerialRead - Receive data" + TextUtil.toHexString(data))
        mTestProvider.receive(data)
    }

    private fun disconnect() {
        Log.i(AppConstant.DEBUG_TAG, "Disconnecting.....")
        connected = Connected.False
        service!!.disconnect()
        usbSerialPort = null
        hearingTestViewModel.disconnect(getString(R.string.device_not_connected))
    }

    private fun connect() {
        connect(null)
    }

    private fun connect(permissionGranted: Boolean?) {
        var usbManager: UsbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val availableDrivers: List<UsbSerialDriver> =
            UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            Log.i(AppConstant.DEBUG_TAG, "ER36 Connection Requested!!!!!")
            return
        }

        // Open a connection to the first available driver.
        val driver: UsbSerialDriver = availableDrivers.get(0)
        val usbDevice: UsbDevice = driver.device

        val hasPermission = usbManager.hasPermission(usbDevice)
        Log.i(AppConstant.DEBUG_TAG, "USB device hasPermission -->" + hasPermission + "Device-- ${usbDevice}")
        if (!hasPermission) {
            val permissionIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
            usbManager.requestPermission(usbDevice, permissionIntent)
            return
        } else {
            val connection: UsbDeviceConnection = usbManager.openDevice(driver.device)
            if (connection == null) {
                Log.i(AppConstant.DEBUG_TAG, "Unable to open connection with device")
                return
            }
            Log.i(AppConstant.DEBUG_TAG, "Connection started... Available ports ${driver.ports}")
            connected = Connected.Pending
            hearingTestViewModel.isDeviceConnected = false
            usbSerialPort = driver.ports.get(0)
            try {
                usbSerialPort!!.open(connection)
                usbSerialPort!!.setParameters(
                    115200,
                    8,
                    UsbSerialPort.STOPBITS_1,
                    UsbSerialPort.PARITY_NONE
                )
                val socket = SerialSocket(applicationContext, connection, usbSerialPort)
                service!!.connect(socket)
                hearingTestViewModel.isDeviceConnected = true
                hearingTestViewModel.scannerConnectionStatus.postValue(true)
                hearingTestViewModel.scanStatusMsg.postValue(getString(R.string.scan_status_ready))
            } catch (e: Exception) {
                Log.i(AppConstant.DEBUG_TAG, "Exception on connecting.")
                onSerialConnectError(e)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isConfigChange", true)
        super.onSaveInstanceState(outState)
    }
}
