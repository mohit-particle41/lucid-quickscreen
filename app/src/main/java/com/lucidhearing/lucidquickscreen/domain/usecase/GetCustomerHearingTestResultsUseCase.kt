package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithHearingTestResult
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository

class GetCustomerHearingTestResultsUseCase(
    private val customerRepository: CustomerRepository
) {
    suspend fun execute(customerId:Long):CustomerWithHearingTestResult{
        return customerRepository.getCustomerHearingTestResults(customerId)
    }
}