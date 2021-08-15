package com.lucidhearing.lucidquickscreen.domain.repository

import com.apollographql.apollo.api.Response
import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.SaveCustomerMutation
import com.lucidhearing.lucidquickscreen.SourceTypesQuery
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithHearingTestResult
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithLifestyleQuestionResponse
import java.util.HashMap

interface CustomerRepository {
    suspend fun saveCustomer(customer: Customer): Long
    suspend fun getCustomerForSync(): List<GrandCentralCustomer>
    suspend fun sampleGetGraphQLRequest(): Response<SourceTypesQuery.Data>?
    suspend fun saveCustomerLifestyleQuestionResponses(
        customerId: Long,
        response: HashMap<String, QuestionResponse>
    ): List<Long>

    suspend fun getCustomerLifestyleQuestionResponses(
        customerId: Long
    ): CustomerWithLifestyleQuestionResponse

    suspend fun saveCustomerHearingTestResult(customerId: Long, result:HashMap<String,ArrayList<ResultFrequency>>):List<Long>
    suspend fun getCustomerHearingTestResults(customerId:Long):CustomerWithHearingTestResult
    suspend fun deleteCustomerFromDBUseCase(customerId:Long)
    suspend fun syncCustomer(grandCentralCustomer: GrandCentralCustomer):Response<SaveCustomerMutation.Data>?
}