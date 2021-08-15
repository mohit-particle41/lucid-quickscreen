package com.lucidhearing.lucidquickscreen.presentation.di

import android.content.Context
import com.lucidhearing.lucidquickscreen.data.prefDataStore.RetailerPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {
    @Provides
    @Singleton
    fun provideRetailerPreferences(@ApplicationContext appContect:Context): RetailerPreferences {
        return RetailerPreferences(appContect)
    }
}