package com.lucidhearing.lucidquickscreen.presentation.setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentStoreSetupBinding
import com.lucidhearing.lucidquickscreen.databinding.FragmentWifiSetupBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.StoreNumberListAdapter
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModel

class StoreSetupFragment : Fragment() {
    private lateinit var fragmentStoreSetupBinding: FragmentStoreSetupBinding
    private lateinit var retailerViewModel: RetailerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentStoreSetupBinding = FragmentStoreSetupBinding.bind(view)
        val setupActivity = (activity as SetupActivity)
        retailerViewModel = setupActivity.retailerViewModel
        val adapter = StoreNumberListAdapter(activity as SetupActivity)
        fragmentStoreSetupBinding.textviewStoreInput.setAdapter(adapter)

    }
}