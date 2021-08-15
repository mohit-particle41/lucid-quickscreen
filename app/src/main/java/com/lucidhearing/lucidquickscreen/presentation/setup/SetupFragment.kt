package com.lucidhearing.lucidquickscreen.presentation.setup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.databinding.FragmentSetupBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SetupFragment : Fragment() {
    private lateinit var fragmentSetupBinding: FragmentSetupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val apikey = (activity as SetupActivity).retailerPreferences.retailerAPIKey.first()
            if (apikey == null){
                val startDestination = findNavController().graph.startDestination
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(startDestination,true).build()
                findNavController().navigate(R.id.action_setupFragment_to_wifiSetupFragment,null,navOptions)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSetupBinding = FragmentSetupBinding.bind(view)
    }
}