package com.lucidhearing.lucidquickscreen.presentation.setup


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lucidhearing.lucidquickscreen.data.prefDataStore.RetailerPreferences
import com.lucidhearing.lucidquickscreen.databinding.ActivitySetupBinding
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetworkAdapter
import com.lucidhearing.lucidquickscreen.presentation.util.showNetworkConnectionNotification
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel.NotificationViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel.NotificationViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModelFactory
import com.lucidhearing.lucidquickscreen.utils.NetworkConnection
import com.lucidhearing.lucidquickscreen.utils.syncService.CustomerDataSyncService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupActivity : AppCompatActivity() {
    @Inject
    lateinit var retailerPreferences: RetailerPreferences
    @Inject
    lateinit var retailerViewModelFactory: RetailerViewModelFactory
    @Inject
    lateinit var notificationViewModelFactory: NotificationViewModelFactory
    @Inject
    lateinit var wifiNetworkAdapter: WifiNetworkAdapter
    @Inject
    lateinit var networkConnection: NetworkConnection
    @Inject
    lateinit var customerDataSyncService: CustomerDataSyncService

    private lateinit var activitySetupBinding: ActivitySetupBinding
    lateinit var retailerViewModel:RetailerViewModel
    lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySetupBinding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(activitySetupBinding.root)
        retailerViewModel = ViewModelProvider(this,retailerViewModelFactory).get(RetailerViewModel::class.java)
        notificationViewModel = ViewModelProvider(this, notificationViewModelFactory)
            .get(NotificationViewModel::class.java)
    }

    fun showNetworkNotification(state: Boolean) {
        showNetworkConnectionNotification(state, activitySetupBinding.setupCoordinatorLayout)
    }
}