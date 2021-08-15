package com.lucidhearing.lucidquickscreen.presentation.di

import android.app.Application
import com.lucidhearing.lucidquickscreen.domain.usecase.*
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel.CustomerViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.hearingTestViewModel.HearingTestViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.lifestyleQuestionViewModel.LifestyleQuestionViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.notificationViewModel.NotificationViewModelFactory
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModel
import com.lucidhearing.lucidquickscreen.presentation.viewmodel.retailerViewModel.RetailerViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {
    @Provides
    @Singleton
    fun provideCustomerViewModelFactory(
        application:Application,
        saveCustomerUseCase: SaveCustomerUseCase,
        saveCustomerLifestyleQuestionResponseUseCase: SaveCustomerLifestyleQuestionResponseUseCase,
        getCustomerLifestyleQuestionResponseUseCase: GetCustomerLifestyleQuestionResponseUseCase,
        saveCustomerHearingTestResultUseCase: SaveCustomerHearingTestResultUseCase,
        getCustomerHearingTestResultUseCase: GetCustomerHearingTestResultsUseCase
    ): CustomerViewModelFactory {
        return CustomerViewModelFactory(
            application,
            saveCustomerUseCase,
            saveCustomerLifestyleQuestionResponseUseCase,
            getCustomerLifestyleQuestionResponseUseCase,
            saveCustomerHearingTestResultUseCase,
            getCustomerHearingTestResultUseCase
        )
    }

    @Provides
    @Singleton
    fun provideLifestyleQuestionViewModelFactory(
        app: Application,
        getAllLifestyleQuestionsUseCase: GetLocalLifestyleQuestionsUseCase,
        saveLifestyleQuestionsToDBUseCase: SaveLifestyleQuestionsToDBUseCase,
        deleteAllLifestyleQuestionsUseCase: DeleteAllLifestyleQuestionsUseCase,
        getRetailerLifestyleQuestionsUseCase: GetRetailerLifestyleQuestionsUseCase
    ):LifestyleQuestionViewModelFactory{
        return LifestyleQuestionViewModelFactory(
            app,
            getAllLifestyleQuestionsUseCase,
            saveLifestyleQuestionsToDBUseCase,
            deleteAllLifestyleQuestionsUseCase,
            getRetailerLifestyleQuestionsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideHearingTestViewModelFactory(app: Application):HearingTestViewModelFactory {
        return HearingTestViewModelFactory(app)
    }

    @Provides
    @Singleton
    fun provideNotificationViewModelFactory():NotificationViewModelFactory{
        return NotificationViewModelFactory()
    }

    @Provides
    @Singleton
    fun provideRetailerViewModelFactory(app: Application):RetailerViewModelFactory{
        return RetailerViewModelFactory(app)
    }
}