package com.lucidhearing.lucidquickscreen.presentation.di

import com.lucidhearing.lucidquickscreen.presentation.adapter.LifestyleQuestionAdapter
import com.lucidhearing.lucidquickscreen.presentation.adapter.ProductRecommendationAdapter
import com.lucidhearing.lucidquickscreen.presentation.adapter.WifiNetworkAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Provides
    @Singleton
    fun provideLifestyleQuestionAdapter(): LifestyleQuestionAdapter {
        return LifestyleQuestionAdapter()
    }

    @Provides
    @Singleton
    fun provideProductRecommendationAdapter():ProductRecommendationAdapter{
        return ProductRecommendationAdapter()
    }

    @Provides
    @Singleton
    fun provideWifiNetworkAdapter():WifiNetworkAdapter{
        return WifiNetworkAdapter()
    }
}