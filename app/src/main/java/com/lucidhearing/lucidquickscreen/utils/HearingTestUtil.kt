package com.lucidhearing.lucidquickscreen.utils

import android.util.Log
import com.github.mikephil.charting.data.Entry
import com.lucid.OAE.*
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import com.lucidhearing.lucidquickscreen.utils.constants.GrandCentralCodeConstant
import java.util.HashMap

object HearingTestUtil {

    const val LEFT_SIDE  = GrandCentralCodeConstant.LEFT_SIDE
    const val RIGHT_SIDE = GrandCentralCodeConstant.RIGHT_SIDE

    const val FREQ_LABEL_2K = "2.0"
    const val FREQ_LABEL_3K = "3.0"
    const val FREQ_LABEL_4K = "4.0"
    const val FREQ_LABEL_5K = "5.0"
    val frequencyReadingList = listOf(FREQ_LABEL_2K, FREQ_LABEL_3K, FREQ_LABEL_4K, FREQ_LABEL_5K)

    const val NO_HEARING_LOSS = "NO LOSS"
    const val SEVERE_HEARING_LOSS = "SEVERE"
    const val MODERATE_HEARING_LOSS = "MODERATE"
    const val BORDERLINE_HEARING_LOSS = "BORDERLINE"
    const val UNDEFINED_HEARING_LOSS = "UNDEFINED"


    const val SEVERE_DP_2K_LIMIT = -1
    const val SEVERE_DP_3K_LIMIT = -3
    const val SEVERE_DP_4K_LIMIT = 0
    const val SEVERE_DP_5K_LIMIT = -2

    const val MODERATE_DP_2K_LIMIT = 0
    const val MODERATE_DP_3K_LIMIT = -2
    const val MODERATE_DP_4K_LIMIT = 1
    const val MODERATE_DP_5K_LIMIT = -1

    const val BORDERLINE_DP_2K_LIMIT = 0
    const val BORDERLINE_DP_3K_LIMIT = -2
    const val BORDERLINE_DP_4K_LIMIT = 1
    const val BORDERLINE_DP_5K_LIMIT = -1

    fun checkHearingLoss(resultList: ArrayList<OAETest>): Boolean {
        resultList.forEach { result ->
            if (result is DPTest) {
                val dpEmissionReading = getDPEmissionReadingHashMap(result.frequencies)
                if(!checkNoHearingLoss(dpEmissionReading)) return true
                Log.i("Test tag","foreach continued " + result.testDate.toString())
            }
        }
        return false
    }

    fun checkHearingLoss_new(resultList: HashMap<String,ArrayList<ResultFrequency>>): Boolean {
        resultList.forEach { (key ,frequencies) ->
                val dpEmissionReading = getDPEmissionReadingHashMap_new(frequencies)
                if(!checkNoHearingLoss(dpEmissionReading)) return true
        }
        return false
    }

    fun compareDPEmission(dpEmission: Int, limit: Int): Boolean {
        return (getDPEmissionFloatValue(dpEmission) <= limit)
    }

    fun getDPEmissionFloatValue(dpEmission: Int): Float {
        return (dpEmission.toFloat() / 256)
    }

    fun getDPEmissionReadingHashMap(frequencies:ArrayList<DPTestFrequency>):HashMap<String, Float>{
        val dpEmissionReading = HashMap<String, Float>()
        frequencies.forEach { frequency ->
            dpEmissionReading.put(
                frequency.FrequencyLabel,
                getDPEmissionFloatValue(frequency.DPTEEmissions)
            )
        }
        return dpEmissionReading
    }
    fun getDPEmissionReadingHashMap_new(frequencies:ArrayList<ResultFrequency>):HashMap<String, Float>{
        val dpEmissionReading = HashMap<String, Float>()
        frequencies.forEach { frequency ->
            dpEmissionReading.put(
                frequency.frequency,
                getDPEmissionFloatValue(frequency.dp)
            )
        }
        return dpEmissionReading
    }

    fun getHearingLossLevel(frequencies: ArrayList<ResultFrequency>): String {
        val dpEmissionReading = getDPEmissionReadingHashMap_new(frequencies)
        if (checkNoHearingLoss(dpEmissionReading)) {
            return NO_HEARING_LOSS
        } else if (checkSevereLoss(dpEmissionReading)) {
            return SEVERE_HEARING_LOSS
        } else if (checkModerateLoss(dpEmissionReading)) {
            return MODERATE_HEARING_LOSS
        } else if (checkBorderlineLoss(dpEmissionReading)) {
            return BORDERLINE_HEARING_LOSS
        } else {
            return UNDEFINED_HEARING_LOSS
        }
    }

//    fun checkSevereLoss1(dpEmissionReading: HashMap<String, Float>): Boolean {
//        var isSevereLoss = false
//        loop@ for (dpEmission in dpEmissionReading) {
//            when (dpEmission.key) {
//                FREQ_LABEL_2K -> {
//                    if (dpEmission.value <= SEVERE_DP_2K_LIMIT) {
//                        isSevereLoss = true
//                        break@loop
//                    }
//                }
//
//                FREQ_LABEL_3K -> {
//                    if (dpEmission.value <= SEVERE_DP_3K_LIMIT) {
//                        isSevereLoss = true
//                        break@loop
//                    }
//                }
//
//                FREQ_LABEL_4K -> {
//                    if (dpEmission.value <= SEVERE_DP_4K_LIMIT) {
//                        isSevereLoss = true
//                        break@loop
//                    }
//                }
//
//                FREQ_LABEL_5K -> {
//                    if (dpEmission.value <= SEVERE_DP_5K_LIMIT) {
//                        isSevereLoss = true
//                        break@loop
//                    }
//                }
//                else -> {
//                }
//            }
//        }
//        return isSevereLoss
//    }

    fun checkSevereLoss(dpEmissionReading: HashMap<String, Float>): Boolean {
        dpEmissionReading.forEach { (freq, reading) ->
            when (freq) {
                FREQ_LABEL_2K -> {
                    if (reading <= SEVERE_DP_2K_LIMIT) return true
                }
                FREQ_LABEL_3K -> {
                    if (reading <= SEVERE_DP_3K_LIMIT) return true
                }
                FREQ_LABEL_4K -> {
                    if (reading <= SEVERE_DP_4K_LIMIT) return true
                }
                FREQ_LABEL_5K -> {
                    if (reading <= SEVERE_DP_5K_LIMIT) return true
                }
            }
        }
        return false
    }

    fun checkModerateLoss(dpEmissionReading: HashMap<String, Float>): Boolean {
        dpEmissionReading.forEach { (freq, reading) ->
            when (freq) {
                FREQ_LABEL_2K -> {
                    if (reading > SEVERE_DP_2K_LIMIT && reading <= MODERATE_DP_2K_LIMIT) return true
                }
                FREQ_LABEL_3K -> {
                    if (reading > SEVERE_DP_3K_LIMIT && reading <= MODERATE_DP_3K_LIMIT) return true
                }
                FREQ_LABEL_4K -> {
                    if (reading > SEVERE_DP_4K_LIMIT && reading <= MODERATE_DP_4K_LIMIT) return true
                }
                FREQ_LABEL_5K -> {
                    if (reading > SEVERE_DP_5K_LIMIT && reading <= MODERATE_DP_5K_LIMIT) return true
                }
            }
        }
        return false
    }

    fun checkBorderlineLoss(dpEmissionReading: HashMap<String, Float>): Boolean {
        dpEmissionReading.forEach { (freq, reading) ->
            when (freq) {
                FREQ_LABEL_2K -> {
                    if (reading > MODERATE_DP_2K_LIMIT && reading <= BORDERLINE_DP_2K_LIMIT) return true
                }
                FREQ_LABEL_3K -> {
                    if (reading > MODERATE_DP_3K_LIMIT && reading <= BORDERLINE_DP_3K_LIMIT) return true
                }
                FREQ_LABEL_4K -> {
                    if (reading > MODERATE_DP_4K_LIMIT && reading <= BORDERLINE_DP_4K_LIMIT) return true
                }
                FREQ_LABEL_5K -> {
                    if (reading > MODERATE_DP_5K_LIMIT && reading <= BORDERLINE_DP_5K_LIMIT) return true
                }
            }
        }
        return false
    }

    fun checkNoHearingLoss(dpEmissionReading: HashMap<String, Float>): Boolean {
        dpEmissionReading.forEach { (freq, reading) ->
            when (freq) {
                FREQ_LABEL_2K -> {
                    if (reading <= BORDERLINE_DP_2K_LIMIT) return false
                }
                FREQ_LABEL_3K -> {
                    if (reading <= BORDERLINE_DP_3K_LIMIT) return false
                }
                FREQ_LABEL_4K -> {
                    if (reading <= BORDERLINE_DP_4K_LIMIT) return false
                }
                FREQ_LABEL_5K -> {
                    if (reading <= BORDERLINE_DP_5K_LIMIT) return false
                }
            }
        }
        return true
    }

    fun prepareGraphData(testResults:ArrayList<OAETest>): HashMap<String,ArrayList<Entry>> {
        val sb: StringBuilder = StringBuilder()
        var resultMap = HashMap<String,ArrayList<Entry>>()
        testResults?.forEachIndexed { index, result ->
            sb.append(result.ToString())
            if (result is DPTest) {
                var resultData = java.util.ArrayList<Entry>()
                result.frequencies.forEach { freq ->
                    val x_axis: Float = (freq.FrequencyLabel + "f").toFloat()
                    val y_axis: Float = getDPEmissionFloatValue(freq.DPTEEmissions)
                    resultData.add(Entry(x_axis, y_axis))
                }
                resultMap.put(
                    if (index == 0) "L" else "R",
                    resultData
                )
            }
        }
        Log.i(AppConstant.DEBUG_TAG, "TestResult ${sb}")
        return resultMap
    }


    fun readFrequencyFromScanner(dpTestFrequency:DPTestFrequencyDisplay):ResultFrequency{
        return ResultFrequency(
            frequency = dpTestFrequency.FrequencyLabel,
            side = dpTestFrequency.ear.toString(),
            l1 = dpTestFrequency.l1,
            l2 = dpTestFrequency.l2,
            dp = dpTestFrequency.DPTEEmissions,
            nf = dpTestFrequency.NF,
            snr = dpTestFrequency.SNR
        )
    }

    fun checkReadingResultIsComplete(result: HashMap<String, ResultFrequency>):Boolean{
        return if(result.size == 4) true else false
    }

    fun prepareHearingTestDataForGraph(testReadings: HashMap<String, ResultFrequency>):ArrayList<Entry>{
        var resultData = java.util.ArrayList<Entry>()
        frequencyReadingList.forEach { freqLabel ->
            val resultFrequency = testReadings.get(freqLabel)
            resultFrequency?.let { freq ->
                val x_axis: Float = (freqLabel + "f").toFloat()
                val y_axis: Float = getDPEmissionFloatValue(freq.dp)
                resultData.add(Entry(x_axis, y_axis))
            }
        }
        return resultData
    }

    fun prepareHearingTestRawData(readings:HashMap<String,ResultFrequency>):ArrayList<ResultFrequency>{
        val resultData = ArrayList<ResultFrequency>()
        frequencyReadingList.forEach { freqLabel->
            val resultFrequency = readings.get(freqLabel)
            resultFrequency?.let { freq ->
                resultData.add(freq)
            }
        }
        return resultData
    }
}
