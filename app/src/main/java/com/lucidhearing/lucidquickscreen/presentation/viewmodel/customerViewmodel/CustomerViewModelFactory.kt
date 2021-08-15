package com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucidhearing.lucidquickscreen.domain.usecase.*

class CustomerViewModelFactory(
    private val app: Application,
    private val saveCustomerUseCase: SaveCustomerUseCase,
    private val saveCustomerLifestyleQuestionResponseUseCase: SaveCustomerLifestyleQuestionResponseUseCase,
    private val getCustomerLifestyleQuestionResponseUseCase: GetCustomerLifestyleQuestionResponseUseCase,
    private val saveCustomerHearingTestResultUseCase: SaveCustomerHearingTestResultUseCase,
    private val getCustomerHearingTestResultUseCase: GetCustomerHearingTestResultsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomerViewModel(
            app,
            saveCustomerUseCase,
            saveCustomerLifestyleQuestionResponseUseCase,
            getCustomerLifestyleQuestionResponseUseCase,
            saveCustomerHearingTestResultUseCase,
            getCustomerHearingTestResultUseCase
        ) as T
    }
}