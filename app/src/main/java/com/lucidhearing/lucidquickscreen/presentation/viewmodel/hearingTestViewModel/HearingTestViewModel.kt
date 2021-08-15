package com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import com.lucid.OAE.DPTest
import com.lucid.OAE.DPTestFrequency
import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.model.entityModel.HearingTestResult
import com.lucidhearing.lucidquickscreen.utils.HearingTestUtil
import com.lucidhearing.lucidquickscreen.utils.usbSerialService.SerialService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.HashMap

class HearingTestViewModel(private val app: Application) : AndroidViewModel(app) {
    var leftSideTestMutableData = MutableLiveData<ArrayList<Entry>>()
    var rightSideTestMutableData = MutableLiveData<ArrayList<Entry>>()
    var readingLeft = HashMap<String,ResultFrequency>()
    var readingRight = HashMap<String,ResultFrequency>()
    private var hearingTestRawResult = HashMap<String,ArrayList<ResultFrequency>>()
    var scanningStatus = MutableLiveData<Boolean>(false)
    var shouldReadTestResult = false
    var scanStatusMsg = MutableLiveData<String>(
        getApplication<Application>()
            .resources.getString(R.string.scan_status_ready)
    )
    var scannerConnectionStatus = MutableLiveData<Boolean>(false)
    var getResultActionState = MutableLiveData<Boolean>(true)
    var firstRun = true
    var mBinder: MutableLiveData<SerialService.SerialBinder?> = MutableLiveData()
    var isDeviceConnected = false

    private var serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            val binder: SerialService.SerialBinder = iBinder as SerialService.SerialBinder
            mBinder.postValue(binder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBinder.postValue(null)
        }
    }

    fun getServiceConnection(): ServiceConnection {
        return serviceConnection
    }

    fun getBinder(): LiveData<SerialService.SerialBinder?> {
        return mBinder
    }

    fun setHearingTestRawResult(result:HashMap<String,ArrayList<ResultFrequency>>){
        this.hearingTestRawResult = result
    }

    fun getHearingTestRawResult():HashMap<String,ArrayList<ResultFrequency>>{
        return this.hearingTestRawResult
    }

    fun prepareHearingTestRawResult(){
        hearingTestRawResult.clear()
        hearingTestRawResult.put(HearingTestUtil.LEFT_SIDE,HearingTestUtil.prepareHearingTestRawData(readingLeft))
        hearingTestRawResult.put(HearingTestUtil.RIGHT_SIDE,HearingTestUtil.prepareHearingTestRawData(readingRight))
    }

    fun initResultDataSet() {
        if (leftSideTestMutableData.value == null) leftSideTestMutableData.postValue(ArrayList())
        if (rightSideTestMutableData.value == null) rightSideTestMutableData.postValue(ArrayList())
    }

    fun clearResultDataSet() {
        leftSideTestMutableData.postValue(ArrayList())
        rightSideTestMutableData.postValue(ArrayList())
        readingLeft = HashMap<String,ResultFrequency>()
        readingRight = HashMap<String,ResultFrequency>()
        hearingTestRawResult = HashMap<String,ArrayList<ResultFrequency>>()
        scanStatusOnClear()
    }

    private fun scanStatusOnClear(){
        scanStatusMsg.postValue(if (isDeviceConnected) getApplication<Application>()
            .resources.getString(R.string.scan_status_ready)
        else
            getApplication<Application>()
                .resources.getString(R.string.device_not_connected))
    }

    fun resetTestData(side:String){
        if(side == HearingTestUtil.LEFT_SIDE) resetLeftSideTestData()
        else if(side == HearingTestUtil.RIGHT_SIDE) resetRightSideTestData()
    }

    fun resetLeftSideTestData(){
        readingLeft.clear()
        leftSideTestMutableData.postValue(ArrayList())
    }

    fun resetRightSideTestData(){
        readingRight.clear()
        rightSideTestMutableData.postValue(ArrayList())
    }

    fun addReadingToResult(resultFrequency: ResultFrequency) {
        if(resultFrequency.side == HearingTestUtil.LEFT_SIDE) addReadingToLeftSideResult(resultFrequency)
        else if(resultFrequency.side == HearingTestUtil.RIGHT_SIDE) addReadingToRightSideResult(resultFrequency)

    }

    fun addReadingToLeftSideResult(resultFrequency: ResultFrequency){
        readingLeft.put(resultFrequency.frequency,resultFrequency)
        if(HearingTestUtil.checkReadingResultIsComplete(readingLeft))
            leftSideTestMutableData.postValue(HearingTestUtil.prepareHearingTestDataForGraph(readingLeft))
    }

    fun addReadingToRightSideResult(resultFrequency: ResultFrequency){
        readingRight.put(resultFrequency.frequency,resultFrequency)
        if(HearingTestUtil.checkReadingResultIsComplete(readingRight))
            rightSideTestMutableData.postValue(HearingTestUtil.prepareHearingTestDataForGraph(readingRight))
    }

    fun disconnect(statusMessage:String){
        isDeviceConnected = false
        scannerConnectionStatus.postValue(false)
        scanningStatus.postValue(false)
        scanStatusMsg.postValue(statusMessage)
    }
}