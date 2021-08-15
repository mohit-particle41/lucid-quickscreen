package com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.lucidhearing.lucidquickscreen.SaveCustomerMutation
import com.lucidhearing.lucidquickscreen.SourceTypesQuery
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerRemoteDataSource
import com.lucidhearing.lucidquickscreen.data.util.SaveCustomerUtil
import com.lucidhearing.lucidquickscreen.type.CustomerInput
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant


class CustomerRemoteDataSourceImpl(
    private val apolloClient:ApolloClient
):CustomerRemoteDataSource {
    override suspend fun sampleGetGraphQLRequest(): Response<SourceTypesQuery.Data>? {
        val response = try {
            apolloClient.query(SourceTypesQuery()).await()
        } catch (e: ApolloException) {
            Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
            null
        }
        return response
    }

    override suspend fun syncCustomer(
        grandCentralCustomer: GrandCentralCustomer
    ): Response<SaveCustomerMutation.Data>? {
        val customerInput = SaveCustomerUtil.prepareSaveCustomerMutationRequest(grandCentralCustomer)
        val response = try {
            apolloClient.mutate(SaveCustomerMutation(customerInput)).await()
        } catch (e: ApolloException) {
            Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
            null
        }
        return response
    }

}