package com.lucidhearing.lucidquickscreen.presentation.di

import com.lucidhearing.lucidquickscreen.data.db.CustomerDAO
import com.lucidhearing.lucidquickscreen.data.db.HearingTestResultDAO
import com.lucidhearing.lucidquickscreen.data.db.LifestyleQuestionDAO
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerLocalDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionLocalDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl.CustomerLocalDataSourceImpl
import com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl.LifestyleQuestionLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Provides
    @Singleton
    fun provideCustomerLocalDataSource(
        customerDAO: CustomerDAO,
        hearingTestResultDAO: HearingTestResultDAO
    ): CustomerLocalDataSource {
        return CustomerLocalDataSourceImpl(customerDAO, hearingTestResultDAO)
    }

    @Provides
    @Singleton
    fun provideLifestyleQuestionLocalDataSource(lifestyleQuestionDAO: LifestyleQuestionDAO): LifestyleQuestionLocalDataSource {
        return LifestyleQuestionLocalDataSourceImpl(lifestyleQuestionDAO)
    }
}