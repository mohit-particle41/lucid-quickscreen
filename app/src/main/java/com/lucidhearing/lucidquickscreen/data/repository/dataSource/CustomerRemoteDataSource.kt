package com.lucidhearing.lucidquickscreen.data.repository.dataSource

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.SaveCustomerMutation
import com.lucidhearing.lucidquickscreen.SourceTypesQuery
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer

interface CustomerRemoteDataSource {
    suspend fun sampleGetGraphQLRequest(): Response<SourceTypesQuery.Data>?
    suspend fun syncCustomer(
        grandCentralCustomer: GrandCentralCustomer
    ): Response<SaveCustomerMutation.Data>?
}