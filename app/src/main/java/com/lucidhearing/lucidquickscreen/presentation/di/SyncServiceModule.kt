package com.lucidhearing.lucidquickscreen.presentation.di

import android.content.Context
import com.lucidhearing.lucidquickscreen.utils.syncService.CustomerDataSyncService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SyncServiceModule {
    @Singleton
    @Provides
    fun provideCustomerDataSyncService(@ApplicationContext appContext:Context): CustomerDataSyncService {
        return CustomerDataSyncService(appContext)
    }
}