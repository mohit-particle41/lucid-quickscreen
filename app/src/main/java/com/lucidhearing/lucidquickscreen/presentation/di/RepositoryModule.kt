package com.lucidhearing.lucidquickscreen.presentation.di

import com.lucidhearing.lucidquickscreen.data.repository.CustomerRepositoryImpl
import com.lucidhearing.lucidquickscreen.data.repository.LifestyleQuestionRepositoryImpl
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerLocalDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerRemoteDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionLocalDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionRemoteDataSource
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository
import com.lucidhearing.lucidquickscreen.domain.repository.LifestyleQuestionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCustomerRepository(
        customerLocalDataSource: CustomerLocalDataSource,
        customerRemoteDataSource: CustomerRemoteDataSource
    ): CustomerRepository {
        return CustomerRepositoryImpl(customerLocalDataSource, customerRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideLifestyleQuestionRepository(
        lifestyleQuestionLocalDataSource: LifestyleQuestionLocalDataSource,
        lifestyleQuestionRemoteDataSource: LifestyleQuestionRemoteDataSource
    ): LifestyleQuestionRepository {
        return LifestyleQuestionRepositoryImpl(
            lifestyleQuestionLocalDataSource,
            lifestyleQuestionRemoteDataSource
        )
    }
}