package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository

class GetCustomerLifestyleQuestionResponseUseCase(
    private val customerRespository: CustomerRepository
) {
    suspend fun execute(customerId: Long): CustomerWithLifestyleQuestionResponse {
        return customerRespository.getCustomerLifestyleQuestionResponses(customerId)
    }
}