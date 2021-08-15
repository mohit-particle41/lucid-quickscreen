package com.lucidhearing.lucidquickscreen.presentation.viewmodel.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedConfirmDialogViewModel: ViewModel() {
    val confirmAction = MutableLiveData<Boolean>()

    fun setConfirmAction(state: Boolean) {
        confirmAction.value = state
    }
}