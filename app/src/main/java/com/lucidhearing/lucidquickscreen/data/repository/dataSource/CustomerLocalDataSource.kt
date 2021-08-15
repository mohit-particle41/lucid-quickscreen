package com.lucidhearing.lucidquickscreen.data.repository.dataSource

import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithHearingTestResult
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithLifestyleQuestionResponse
import java.util.HashMap

interface CustomerLocalDataSource {
    suspend fun saveCustomerToDB(customer: Customer): Long
    suspend fun getCustomerForSync(): List<GrandCentralCustomer>
    suspend fun saveCustomerLifestyleQuestionResponses(
        customerId: Long,
        responses: HashMap<String, QuestionResponse>
    ): List<Long>

    suspend fun getCustomerLifestyleQuestionResponses(
        customerId: Long
    ): CustomerWithLifestyleQuestionResponse

    suspend fun saveCustomerHearingTestResults(customerId: Long, result:HashMap<String,ArrayList<ResultFrequency>>):List<Long>
    suspend fun getCustomerHearingTestResults(customerId:Long):CustomerWithHearingTestResult
    suspend fun deleteCustomerFromDBUseCase(customerId:Long)
}