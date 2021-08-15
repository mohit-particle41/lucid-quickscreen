package com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationViewModel() as T
    }
}