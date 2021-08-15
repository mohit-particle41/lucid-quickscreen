package com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RetailerViewModelFactory(
    private val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RetailerViewModel(app) as T
    }
}