package com.lucidhearing.lucidquickscreen.presentation.di

import com.apollographql.apollo.ApolloClient
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerRemoteDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionRemoteDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl.CustomerRemoteDataSourceImpl
import com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl.LifestyleQuestionRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Provides
    @Singleton
    fun provideCustomerRemoteDataSource(apolloClient: ApolloClient):CustomerRemoteDataSource{
        return CustomerRemoteDataSourceImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideLifestyleQuestionRemoteDataSource(apolloClient: ApolloClient):LifestyleQuestionRemoteDataSource{
        return LifestyleQuestionRemoteDataSourceImpl(apolloClient)
    }
}