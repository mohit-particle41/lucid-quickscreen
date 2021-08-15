package com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HearingTestViewModelFactory(private val app: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HearingTestViewModel(app) as T
    }
}