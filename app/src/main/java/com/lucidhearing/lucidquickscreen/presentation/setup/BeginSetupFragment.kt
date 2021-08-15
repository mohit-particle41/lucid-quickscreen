package com.lucidhearing.lucidquickscreen.presentation.setup


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentBeginSetupBinding
import com.lucidhearing.lucidquickscreen.presentation.util.showAlert
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModel
import com.lucidhearing.lucidquickscreen.utils.AppUtils


class BeginSetupFragment : Fragment() {
    private lateinit var fragmentBeginSetupBinding: FragmentBeginSetupBinding
    private lateinit var retailerViewModel: RetailerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_begin_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBeginSetupBinding = FragmentBeginSetupBinding.bind(view)
        val setupActivity = (activity as SetupActivity)
        retailerViewModel = setupActivity.retailerViewModel

        fragmentBeginSetupBinding.buttonBeginSetup.setOnClickListener {
            beginSetupAction()
        }

    }

    private fun beginSetupAction(){
        if(AppUtils.isNetworkAvailable((activity as SetupActivity).applicationContext)){
            findNavController().navigate(R.id.action_beginSetupFragment_to_storeSetupFragment)
        }else{
            showAlert(getString(R.string.alert_network_not_connected))
        }
    }

}