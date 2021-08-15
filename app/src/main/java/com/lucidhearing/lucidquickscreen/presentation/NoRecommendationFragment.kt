package com.lucidhearing.lucidquickscreen.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentNoRecommendationBinding
import com.lucidhearing.lucidquickscreen.presentation.util.AlertDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.LoadingDialog
import com.lucidhearing.lucidquickscreen.presentation.util.listener.SaveCustomerDetailsListener
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel.HearingTestViewModel
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant

class NoRecommendationFragment : Fragment(), SaveCustomerDetailsListener {
    lateinit var fragmentNoRecommenationBinding:FragmentNoRecommendationBinding
    lateinit var customerViewModel: CustomerViewModel
    lateinit var hearingTestViewModel: HearingTestViewModel
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_recommendation, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNoRecommenationBinding = FragmentNoRecommendationBinding.bind(view)
        val homeActivity = (activity as HomeActivity)
        customerViewModel = homeActivity.customerViewModel
        hearingTestViewModel = homeActivity.hearingTestViewModel
        customerViewModel.saveCustomerDetailsListener = this
        loadingDialog = LoadingDialog(homeActivity)
        addFinishButtonListner()
        observeViewModelLoadingState()
    }

    private fun addFinishButtonListner(){
        fragmentNoRecommenationBinding.buttonNext.setOnClickListener {
            val testResult = hearingTestViewModel.getHearingTestRawResult()
            customerViewModel.saveCustomerDetails(testResult)
        }
    }

    private fun observeViewModelLoadingState(){
        customerViewModel.loadingState.observe(viewLifecycleOwner,{ state ->
            if(state) loadingDialog.startLoadingDialog() else loadingDialog.dismissDialog()
        })
    }

    override fun onSaveCustomerDetailsSuccess() {
        customerViewModel.getCustomerTestResults()
        customerViewModel.getCustomerLifestyleQuestionResponse()
        customerViewModel.clearCustomerDetails()
        hearingTestViewModel.clearResultDataSet()
        findNavController().popBackStack(R.id.homeFragment,false)
    }

    override fun onSaveCustomerDetailsError(message: String) {
        showAlert(message)
    }

    private fun showAlert(message:String){
        val alertDialog = AlertDialogFragment()
        alertDialog.message = message
        alertDialog.show(childFragmentManager, AlertDialogFragment.TAG)
    }
}