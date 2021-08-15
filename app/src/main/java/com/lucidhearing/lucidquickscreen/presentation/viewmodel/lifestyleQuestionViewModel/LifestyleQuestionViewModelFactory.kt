package com.lucidhearing.lucidquickscreen.presentation.viewmodel.lifestyleQuestionViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucidhearing.lucidquickscreen.domain.usecase.DeleteAllLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.GetLocalLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.GetRetailerLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.SaveLifestyleQuestionsToDBUseCase

class LifestyleQuestionViewModelFactory(
    private val app: Application,
    private val getAllLifestyleQuestionsUseCase: GetLocalLifestyleQuestionsUseCase,
    private val saveLifestyleQuestionsToDBUseCase: SaveLifestyleQuestionsToDBUseCase,
    private val deleteAllLifestyleQuestionsUseCase: DeleteAllLifestyleQuestionsUseCase,
    private val getRetailerLifestyleQuestionsUseCase: GetRetailerLifestyleQuestionsUseCase
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LifestyleQuestionViewModel(
            app,
            getAllLifestyleQuestionsUseCase,
            saveLifestyleQuestionsToDBUseCase,
            deleteAllLifestyleQuestionsUseCase,
            getRetailerLifestyleQuestionsUseCase
        ) as T
    }
}