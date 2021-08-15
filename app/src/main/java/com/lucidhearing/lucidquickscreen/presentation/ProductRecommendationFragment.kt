package com.lucidhearing.lucidquickscreen.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel.HearingTestViewModel
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.data.model.dataModel.RetailerProduct
import com.lucidhearing.lucidquickscreen.databinding.FragmentProductRecommendationBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.ProductRecommendationAdapter
import com.lucidhearing.lucidquickscreen.presentation.util.AlertDialogFragment
import com.lucidhearing.lucidquickscreen.presentation.util.LoadingDialog
import com.lucidhearing.lucidquickscreen.presentation.util.listener.SaveCustomerDetailsListener
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant

class ProductRecommendationFragment : Fragment(), SaveCustomerDetailsListener {

    private lateinit var fragmentProductRecommendationBinding: FragmentProductRecommendationBinding
    lateinit var customerViewModel: CustomerViewModel
    lateinit var hearingTestViewModel: HearingTestViewModel
    lateinit var productRecommendationAdapter: ProductRecommendationAdapter
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
        return inflater.inflate(R.layout.fragment_product_recommendation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentProductRecommendationBinding = FragmentProductRecommendationBinding.bind(view)
        val homeActivity = (activity as HomeActivity)
        customerViewModel = homeActivity.customerViewModel
        hearingTestViewModel = homeActivity.hearingTestViewModel
        productRecommendationAdapter = homeActivity.productRecommendationAdapter
        loadingDialog = LoadingDialog(homeActivity)
        customerViewModel.saveCustomerDetailsListener = this
        initProductRecommendationList()
        observeViewModelLoadingState()
        addFinishButtonListner()
    }

    private fun initProductRecommendationList(){
        fragmentProductRecommendationBinding.rvProductList.apply {
            productRecommendationAdapter.productRecommendations = customerViewModel.retailerProducts
            if(customerViewModel.retailerProducts.size == 0) customerViewModel.getProductRecommendation()
            productRecommendationAdapter.productRecommendations = customerViewModel.retailerProducts
            adapter = productRecommendationAdapter
        }
    }

    private fun observeViewModelLoadingState(){
        customerViewModel.loadingState.observe(viewLifecycleOwner,{ state ->
            if(state) loadingDialog.startLoadingDialog() else loadingDialog.dismissDialog()
        })
    }

    private fun addFinishButtonListner(){
        fragmentProductRecommendationBinding.buttonNext.setOnClickListener {
            val testResult = hearingTestViewModel.getHearingTestRawResult()
            customerViewModel.saveCustomerDetails(testResult)
        }
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