package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository
import java.util.HashMap

class SaveCustomerLifestyleQuestionResponseUseCase(
    private val customerRespository: CustomerRepository
) {
    suspend fun execute(customerId: Long, response:HashMap<String, QuestionResponse>): List<Long> {
        return customerRespository.saveCustomerLifestyleQuestionResponses(customerId, response)
    }
}