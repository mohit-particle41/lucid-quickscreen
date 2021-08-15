package com.lucidhearing.lucidquickscreen.presentation.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.lucidhearing.lucidquickscreen.data.api.CustomerAPIService
import com.lucidhearing.lucidquickscreen.data.api.interceptor.AuthorizationInterceptor
import com.lucidhearing.lucidquickscreen.data.prefDataStore.RetailerPreferences
import com.lucidhearing.lucidquickscreen.type.CustomType
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("")
            .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(authorizationInterceptor: AuthorizationInterceptor):ApolloClient{
        val sdfISO8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        val dateCustomTypeAdapter = object : CustomTypeAdapter<Date> {
            override fun decode(value: CustomTypeValue<*>): Date {
                return try {
                    sdfISO8601.parse(value.value.toString())
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }
            }

            override fun encode(value: Date): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(sdfISO8601.format(value))
            }
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()

        return ApolloClient.builder()
            .serverUrl(AppConstant.GRAPHQL_BASE_URL)
            .addCustomTypeAdapter(CustomType.DATETIME,dateCustomTypeAdapter)
            .okHttpClient(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomerAPIService(retrofit: Retrofit):CustomerAPIService{
        return retrofit.create(CustomerAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(retailerPreferences: RetailerPreferences):AuthorizationInterceptor{
        return AuthorizationInterceptor(retailerPreferences)
    }

}