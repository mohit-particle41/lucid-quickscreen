package com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.lucidhearing.lucidquickscreen.GetRetailerQuestionsQuery
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionRemoteDataSource
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant

class LifestyleQuestionRemoteDataSourceImpl(
    private val apolloClient: ApolloClient
    ):LifestyleQuestionRemoteDataSource {
    override suspend fun getRetailerLifestyleQuestions():Response<GetRetailerQuestionsQuery.Data>? {
        val response = try {
            apolloClient.query(GetRetailerQuestionsQuery(
                Input.optional("WALGREENS"),
                Input.optional("LA"))).await()
        }catch (e: ApolloException){
            Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
            null
        }
        return response
    }
}