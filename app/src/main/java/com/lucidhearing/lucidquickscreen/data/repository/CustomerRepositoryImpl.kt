package com.lucidhearing.lucidquickscreen.data.repository

import com.apollographql.apollo.api.Response
import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.SaveCustomerMutation
import com.lucidhearing.lucidquickscreen.SourceTypesQuery
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithHearingTestResult
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerLocalDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerRemoteDataSource
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository
import java.util.HashMap

class CustomerRepositoryImpl(
    private val customerLocalDataSource: CustomerLocalDataSource,
    private val customerRemoteDataSource: CustomerRemoteDataSource
) : CustomerRepository {
    override suspend fun saveCustomer(customer: Customer): Long {
        return customerLocalDataSource.saveCustomerToDB(customer)
    }

    override suspend fun getCustomerForSync(): List<GrandCentralCustomer> {
        return customerLocalDataSource.getCustomerForSync()
    }

    override suspend fun sampleGetGraphQLRequest(): Response<SourceTypesQuery.Data>? {
        return customerRemoteDataSource.sampleGetGraphQLRequest()
    }

    override suspend fun saveCustomerLifestyleQuestionResponses(
        customerId: Long,
        response: HashMap<String, QuestionResponse>
    ): List<Long> {
        return customerLocalDataSource.saveCustomerLifestyleQuestionResponses(customerId, response)
    }

    override suspend fun getCustomerLifestyleQuestionResponses(
        customerId: Long
    ): CustomerWithLifestyleQuestionResponse {
        return customerLocalDataSource.getCustomerLifestyleQuestionResponses(customerId)
    }

    override suspend fun saveCustomerHearingTestResult(customerId: Long,result: HashMap<String,ArrayList<ResultFrequency>>): List<Long> {
        return customerLocalDataSource.saveCustomerHearingTestResults(customerId, result)
    }

    override suspend fun getCustomerHearingTestResults(customerId: Long): CustomerWithHearingTestResult {
        return customerLocalDataSource.getCustomerHearingTestResults(customerId)
    }

    override suspend fun deleteCustomerFromDBUseCase(customerId: Long) {
        customerLocalDataSource.deleteCustomerFromDBUseCase(customerId)
    }

    override suspend fun syncCustomer(
        grandCentralCustomer: GrandCentralCustomer
    ): Response<SaveCustomerMutation.Data>? {
        return customerRemoteDataSource.syncCustomer(grandCentralCustomer)
    }

}