package com.lucidhearing.lucidquickscreen.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentResultGraphBinding
import com.lucidhearing.lucidquickscreen.presentation.util.AlertDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel.HearingTestViewModel
import com.lucidhearing.lucidquickscreen.utils.HearingTestUtil
import com.lucidhearing.lucidquickscreen.utils.constants.GraphConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.listOf


class ResultGraphFragment : Fragment(){

    private lateinit var fragmentResultGraphBinding: FragmentResultGraphBinding
    lateinit var customerViewModel: CustomerViewModel
    lateinit var hearingTestViewModel: HearingTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentResultGraphBinding = FragmentResultGraphBinding.bind(view)
        val homeActivity = (activity as HomeActivity)
        customerViewModel = homeActivity.customerViewModel
        hearingTestViewModel = homeActivity.hearingTestViewModel

        addButtonListener()
        observeTestResultData()
        setScanningStatus()
        initResultGraph()
    }

    override fun onResume() {
        super.onResume()
        hearingTestViewModel.shouldReadTestResult = true
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initResultGraph(){
        setupGraphStyles()
        hearingTestViewModel.initResultDataSet()
    }

    private fun addButtonListener(){
        fragmentResultGraphBinding.buttonNext.setOnClickListener {
            hearingTestViewModel.shouldReadTestResult = false
            hearingTestViewModel.prepareHearingTestRawResult()
            if(HearingTestUtil.checkHearingLoss_new(hearingTestViewModel.getHearingTestRawResult())){
                findNavController().navigate(R.id.action_resultGraphFragment_to_productRecommendationFragment)
            }else{
                findNavController().navigate(R.id.action_resultGraphFragment_to_noRecommendationFragment)
            }
        }

        fragmentResultGraphBinding.buttonRescan.setOnClickListener {
            restartScan()
        }
    }

    private fun observeTestResultData(){
        hearingTestViewModel.leftSideTestMutableData.observe(viewLifecycleOwner,{ data ->
            setupGraphLeft(data)
        })
        hearingTestViewModel.rightSideTestMutableData.observe(viewLifecycleOwner,{ data ->
            setupGraphRight(data)
        })
    }

    private fun setScanningStatus(){
        fragmentResultGraphBinding.textviewScanStatus.text = if (hearingTestViewModel.isDeviceConnected)
            getString(R.string.scan_status_ready) else getString(R.string.device_not_connected)

        hearingTestViewModel.scanStatusMsg.observe(viewLifecycleOwner,{ msg ->
//            if (hearingTestViewModel.isDeviceConnected)
                fragmentResultGraphBinding.textviewScanStatus.text = msg
        })

        hearingTestViewModel.scanningStatus.observe(viewLifecycleOwner,{status ->
            setProgressBarVisibility(status)
        })
    }

    private fun setupGraphStyles(){
        fragmentResultGraphBinding.resultChartLeft.apply {
            axisLeft.isEnabled = GraphConstants.AXIS_LEFT
            axisRight.isEnabled = GraphConstants.AXIS_RIGHT
            xAxis.isEnabled = GraphConstants.AXIS_X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(GraphConstants.DRAW_GRID_LINE)
            xAxis.axisMinimum = GraphConstants.AXIS_X_MINIMUM
            xAxis.axisMaximum = GraphConstants.AXIS_X_MAXIMUM
            xAxis.granularity = GraphConstants.AXIS_X_GRANULARITY
            xAxis.setLabelCount(GraphConstants.AXIS_X_LABEL_COUNT,true)
            xAxis.valueFormatter = AxisLabelFormatter()
            xAxis.textColor = GraphConstants.AXIS_X_TEXT_COLOR
            xAxis.textSize = GraphConstants.AXIS_X_TEXT_SIZE
            axisLeft.axisMinimum = GraphConstants.AXIS_LEFT_MINIMUM
            axisLeft.axisMaximum = GraphConstants.AXIS_LEFT_MAXIMUM
            axisLeft.granularity = GraphConstants.AXIS_LEFT_GRANULARITY
            axisLeft.enableGridDashedLine(
                GraphConstants.DASHED_GRID_LINE_LENGTH,
                GraphConstants.DASHED_GRID_LINE_SPACE,
                GraphConstants.DASHED_GRID_LINE_PHASE)
            axisLeft.setLabelCount(GraphConstants.AXIS_LEFT_LABEL_COUNT,true)
            axisLeft.valueFormatter = AxisLabelFormatter()
            axisLeft.textColor = GraphConstants.AXIS_LEFT_TEXT_COLOR
            axisLeft.textSize = GraphConstants.AXIS_LEFT_TEXT_SIZE
            legend.isEnabled = GraphConstants.LEGEND
            description.isEnabled = GraphConstants.DESCRIPTION
            isDragEnabled = GraphConstants.DRAG
            setTouchEnabled(GraphConstants.TOUCH)
            setScaleEnabled(GraphConstants.SCALE)
            setPinchZoom(GraphConstants.PINCH)
        }
        fragmentResultGraphBinding.resultChartRight.apply {
            axisLeft.isEnabled = GraphConstants.AXIS_LEFT
            axisRight.isEnabled = GraphConstants.AXIS_RIGHT
            xAxis.isEnabled = GraphConstants.AXIS_X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(GraphConstants.DRAW_GRID_LINE)
            xAxis.axisMinimum = GraphConstants.AXIS_X_MINIMUM
            xAxis.axisMaximum = GraphConstants.AXIS_X_MAXIMUM
            xAxis.granularity = GraphConstants.AXIS_X_GRANULARITY
            xAxis.setLabelCount(GraphConstants.AXIS_X_LABEL_COUNT,true)
            xAxis.valueFormatter = AxisLabelFormatter()
            xAxis.textColor = GraphConstants.AXIS_X_TEXT_COLOR
            xAxis.textSize = GraphConstants.AXIS_X_TEXT_SIZE
            axisLeft.axisMinimum = GraphConstants.AXIS_LEFT_MINIMUM
            axisLeft.axisMaximum = GraphConstants.AXIS_LEFT_MAXIMUM
            axisLeft.granularity = GraphConstants.AXIS_LEFT_GRANULARITY
            axisLeft.enableGridDashedLine(
                GraphConstants.DASHED_GRID_LINE_LENGTH,
                GraphConstants.DASHED_GRID_LINE_SPACE,
                GraphConstants.DASHED_GRID_LINE_PHASE)
            axisLeft.setLabelCount(GraphConstants.AXIS_LEFT_LABEL_COUNT,true)
            axisLeft.valueFormatter = AxisLabelFormatter()
            axisLeft.textColor = GraphConstants.AXIS_LEFT_TEXT_COLOR
            axisLeft.textSize = GraphConstants.AXIS_LEFT_TEXT_SIZE
            legend.isEnabled = GraphConstants.LEGEND
            description.isEnabled = GraphConstants.DESCRIPTION
            isDragEnabled = GraphConstants.DRAG
            setTouchEnabled(GraphConstants.TOUCH)
            setScaleEnabled(GraphConstants.SCALE)
            setPinchZoom(GraphConstants.PINCH)
        }
    }

    private fun setupGraphLeft(resultData: ArrayList<Entry>){
        val lineDataSet = LineDataSet(resultData,GraphConstants.LABEL_LEFT)
        lineDataSet.apply {
            color = GraphConstants.DATA_LINE_COLOR_LEFT
            valueTextColor = GraphConstants.DATA_LINE_VALUE_TEXT_COLOR
            highLightColor = GraphConstants.DATA_LINE_VALUE_HIGHLIGHT_COLOR_LEFT
            setDrawValues(GraphConstants.DATA_LINE_SET_DRAW_VALUES)
            lineWidth = GraphConstants.DATA_LINE_WIDTH
            isHighlightEnabled = GraphConstants.DATA_LINE_HIGHLIGHT_ENABLED
            setDrawHighlightIndicators(GraphConstants.DATA_LINE_HIGHLIGHT_INDICATORS)
        }
        val dataLines:List<LineDataSet> = listOf(lineDataSet)
        val graphData = LineData(dataLines)
        fragmentResultGraphBinding.resultChartLeft.data = graphData
        fragmentResultGraphBinding.resultChartLeft.animateXY(
            GraphConstants.ANIMATE_X_DURATION_MS, GraphConstants.ANIMATE_Y_DURATION_MS)
        setNextButtonSectionVisibility()
    }

    private fun setupGraphRight(resultData: ArrayList<Entry>){
            val lineDataSet = LineDataSet(resultData, GraphConstants.LABEL_RIGHT)
            lineDataSet.apply {
                color = GraphConstants.DATA_LINE_COLOR_RIGHT
                valueTextColor = GraphConstants.DATA_LINE_VALUE_TEXT_COLOR
                highLightColor = GraphConstants.DATA_LINE_VALUE_HIGHLIGHT_COLOR_RIGHT
                setDrawValues(GraphConstants.DATA_LINE_SET_DRAW_VALUES)
                lineWidth = GraphConstants.DATA_LINE_WIDTH
                isHighlightEnabled = GraphConstants.DATA_LINE_HIGHLIGHT_ENABLED
                setDrawHighlightIndicators(GraphConstants.DATA_LINE_HIGHLIGHT_INDICATORS)
            }
            val dataLines: List<LineDataSet> = listOf(lineDataSet)
            val graphData = LineData(dataLines)
            fragmentResultGraphBinding.resultChartRight.data = graphData
            fragmentResultGraphBinding.resultChartRight.animateXY(
                GraphConstants.ANIMATE_X_DURATION_MS, GraphConstants.ANIMATE_Y_DURATION_MS)
            setNextButtonSectionVisibility()
    }

    private fun setNextButtonSectionVisibility(){
        val leftResultData = hearingTestViewModel.leftSideTestMutableData.value ?: ArrayList()
        val rightResultData = hearingTestViewModel.rightSideTestMutableData.value ?: ArrayList()
        if(leftResultData.size > 0 && rightResultData.size > 0){
            setNextSectionVisibility(true)
            setProgressBarVisibility(false)
            hearingTestViewModel.scanningStatus.postValue(false)
            hearingTestViewModel.scanStatusMsg.postValue(getString(R.string.scan_status_complete))
        }else{
            setNextSectionVisibility(false)
        }
    }

    private fun restartScan(){
        hearingTestViewModel.getResultActionState.postValue(true)
    }

    private fun showAlert(message:String){
        val alertDialog = AlertDialogFragment()
        alertDialog.message = message
        alertDialog.show(childFragmentManager, AlertDialogFragment.TAG)
    }

    private fun setProgressBarVisibility(state:Boolean){
        lifecycleScope.launch(Dispatchers.Main){
            fragmentResultGraphBinding.progressBarScan.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    private fun setNextSectionVisibility(state:Boolean){
        lifecycleScope.launch(Dispatchers.Main){
            fragmentResultGraphBinding.sectionNextButton.visibility = if (state) View.VISIBLE else View.GONE
        }
    }
}

class AxisLabelFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return super.getAxisLabel(value, axis)
    }
}
