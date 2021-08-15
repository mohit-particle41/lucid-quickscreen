package com.lucidhearing.lucidquickscreen.presentation.di

import android.content.Context
import com.lucidhearing.lucidquickscreen.utils.NetworkConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationHelperModule {

    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext appContext: Context):NetworkConnection{
        return NetworkConnection(appContext)
    }
}