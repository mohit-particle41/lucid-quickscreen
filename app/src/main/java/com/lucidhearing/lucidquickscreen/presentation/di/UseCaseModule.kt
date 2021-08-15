package com.lucidhearing.lucidquickscreen.presentation.di

import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository
import com.lucidhearing.lucidquickscreen.domain.repository.LifestyleQuestionRepository
import com.lucidhearing.lucidquickscreen.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideSaveCustomerUseCase(customerRepository: CustomerRepository):SaveCustomerUseCase{
        return SaveCustomerUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideGetCustomerListForSyncUseCase(
        customerRepository: CustomerRepository
    ):GetCustomerListForSyncUseCase{
        return GetCustomerListForSyncUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideGetLocalLifestyleQuestionsUseCase(
        lifestyleQuestionRepository: LifestyleQuestionRepository
    ):GetLocalLifestyleQuestionsUseCase{
        return GetLocalLifestyleQuestionsUseCase(lifestyleQuestionRepository)
    }

    @Provides
    @Singleton
    fun provideSaveLifestyleQuestionsUseCase(
        lifestyleQuestionRepository: LifestyleQuestionRepository
    ):SaveLifestyleQuestionsToDBUseCase{
        return SaveLifestyleQuestionsToDBUseCase(lifestyleQuestionRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteAllLifestyleQuestionUseCase(
        lifestyleQuestionRepository: LifestyleQuestionRepository
    ):DeleteAllLifestyleQuestionsUseCase{
        return DeleteAllLifestyleQuestionsUseCase(lifestyleQuestionRepository)
    }

    @Provides
    @Singleton
    fun provideSaveCustomerLifestyleQuestionResponseUseCase(
        customerRepository: CustomerRepository
    ):SaveCustomerLifestyleQuestionResponseUseCase{
        return SaveCustomerLifestyleQuestionResponseUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideGetCustomerLifestyleQuestionResponseUseCase(
        customerRepository: CustomerRepository
    ):GetCustomerLifestyleQuestionResponseUseCase{
        return GetCustomerLifestyleQuestionResponseUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideSaveCustomerHearingTestResultUseCase(
        customerRepository: CustomerRepository
    ):SaveCustomerHearingTestResultUseCase{
        return SaveCustomerHearingTestResultUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideGetCustomerHearingTestResultsUseCase(
        customerRepository: CustomerRepository
    ):GetCustomerHearingTestResultsUseCase{
        return GetCustomerHearingTestResultsUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteCustomerFromDBUseCase(
        customerRepository: CustomerRepository
    ):DeleteCustomerFromDBUseCase{
        return DeleteCustomerFromDBUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideSyncCustomerUseCase(
        customerRepository: CustomerRepository
    ):SyncCustomerUseCase{
        return SyncCustomerUseCase(customerRepository)
    }

    @Provides
    @Singleton
    fun provideGetRetailerLifestyleQuestionsUseCase(
        lifestyleQuestionRepository: LifestyleQuestionRepository
    ):GetRetailerLifestyleQuestionsUseCase{
        return GetRetailerLifestyleQuestionsUseCase(lifestyleQuestionRepository)
    }
}