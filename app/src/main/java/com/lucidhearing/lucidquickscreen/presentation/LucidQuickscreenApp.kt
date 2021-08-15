package com.lucidhearing.lucidquickscreen.presentation

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import com.lucidhearing.lucidquickscreen.utils.syncService.CustomerDataSyncService
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LucidQuickscreenApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var customerDataSyncService: CustomerDataSyncService

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()
        AppCenter.start(
            this, AppConstant.APP_CENTER_SDK_KEY,
            Analytics::class.java, Crashes::class.java
        )
        //Disable sync service
        customerDataSyncService.checkOrCreateDataSyncWorkRequest()
    }
}

/*
https://www.raywenderlich.com/18348259-datastore-tutorial-for-android-getting-started
https://www.youtube.com/watch?v=r2McC4-2CoI
https://shubham08gupta.medium.com/using-android-jetpacks-preferencestorage-with-dagger-hilt-17023a10d737
https://developer.android.com/codelabs/android-preferences-datastore#5
 */