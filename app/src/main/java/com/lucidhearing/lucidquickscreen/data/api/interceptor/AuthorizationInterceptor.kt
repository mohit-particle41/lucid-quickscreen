package com.lucidhearing.lucidquickscreen.data.api.interceptor

import com.lucidhearing.lucidquickscreen.data.prefDataStore.RetailerPreferences
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val retailerPreferences: RetailerPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var apiKey:String = ""
        runBlocking {
            apiKey = retailerPreferences.retailerAPIKey.first() ?: ""
        }
        val request = chain.request().newBuilder()
            .addHeader(AppConstant.APIM_SUBSCRIPTION_KEY_HEADER, "d88408b6abde45709f0b75fcff6bcb0c")
            .build()
        return chain.proceed(request)
    }
}

