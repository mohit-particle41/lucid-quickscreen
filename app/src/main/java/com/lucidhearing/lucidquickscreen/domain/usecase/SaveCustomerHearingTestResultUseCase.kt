package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository
import java.util.HashMap

class SaveCustomerHearingTestResultUseCase(
    private val customerRepository: CustomerRepository
) {
    suspend fun execute(customerId: Long, result: HashMap<String, ArrayList<ResultFrequency>>):List<Long>{
        return customerRepository.saveCustomerHearingTestResult(customerId, result)
    }
}