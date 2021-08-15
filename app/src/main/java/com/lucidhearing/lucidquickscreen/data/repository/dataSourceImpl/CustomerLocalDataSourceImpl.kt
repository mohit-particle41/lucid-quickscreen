package com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl

import android.util.Log
import com.lucid.OAE.DPTest
import com.lucid.OAE.DPTestFrequency
import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.data.db.CustomerDAO
import com.lucidhearing.lucidquickscreen.data.db.HearingTestResultDAO
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.HearingTestResult
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithHearingTestResult
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.CustomerLocalDataSource
import com.lucidhearing.lucidquickscreen.utils.HearingTestUtil
import java.util.*
import kotlin.collections.ArrayList

class CustomerLocalDataSourceImpl(
    private val customerDAO: CustomerDAO,
    private val hearingTestResultDAO: HearingTestResultDAO
) : CustomerLocalDataSource {
    override suspend fun saveCustomerToDB(customer: Customer): Long {
        return customerDAO.saveCustomer(customer)
    }

    override suspend fun getCustomerForSync(): List<GrandCentralCustomer> {
        val grandCentralCustomers:MutableList<GrandCentralCustomer> = mutableListOf()
        val customers = customerDAO.getCustomerForSync()
        customers.forEach{ customer ->
            val questionResponses = customerDAO.getCustomerWithLifestyleQuestionResponses(customer.id!!)
            val testResults = customerDAO.getCustomerWithHearingTestResults(customer.id!!)
            grandCentralCustomers.add(
                GrandCentralCustomer(
                    customer = customer,
                    lifestyleQueResponse = questionResponses.questionResponses,
                    hearingTestResult = testResults.hearingTestResults
                )
            )
        }
        return grandCentralCustomers
    }

    override suspend fun saveCustomerLifestyleQuestionResponses(
        customerId: Long,
        response: HashMap<String, QuestionResponse>
    ): List<Long> {
        val customerResponses: MutableList<CustomerLifestyleQuestionResponse> = mutableListOf()
        response.forEach { (questionCode, questionResponse) ->
            val res = CustomerLifestyleQuestionResponse(
                customerId = customerId,
                questionCode = questionResponse.questionCode,
                response = questionResponse.response,
                createdAt = Date()
            )
            customerResponses.add(res)
        }
        return customerDAO.saveCustomerQuestionResponses(customerResponses)

    }

    override suspend fun getCustomerLifestyleQuestionResponses(
        customerId: Long
    ): CustomerWithLifestyleQuestionResponse {
        return customerDAO.getCustomerWithLifestyleQuestionResponses(customerId)
    }

    override suspend fun saveCustomerHearingTestResults(
        customerId: Long,
        result: HashMap<String,ArrayList<ResultFrequency>>): List<Long> {
        var resultList:MutableList<HearingTestResult> = mutableListOf()
        result.forEach{(key,oaeTestResult) ->
                resultList.add(HearingTestResult(
                    customerId = customerId,
                    side = key,
                    hearingLossLevel = HearingTestUtil.getHearingLossLevel(oaeTestResult),
                    frequency = oaeTestResult,
                    createdDate = Date()
                ))
        }
        return hearingTestResultDAO.saveHearingTestResults(resultList)
    }

    override suspend fun getCustomerHearingTestResults(customerId: Long): CustomerWithHearingTestResult {
        return customerDAO.getCustomerWithHearingTestResults(customerId)
    }

    override suspend fun deleteCustomerFromDBUseCase(customerId: Long) {
        hearingTestResultDAO.deleteCustomerHearingTestResults(customerId)
        customerDAO.deleteCustomerLifestyleQuestionResponse(customerId)
        customerDAO.deleteCustomerByID(customerId)
    }
}