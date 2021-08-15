package com.lucidhearing.lucidquickscreen.domain.usecase

import android.util.Log
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository

class DeleteCustomerFromDBUseCase(
    private val customerRepository: CustomerRepository
) {
    suspend fun execute(customerID:Long){
        customerRepository.deleteCustomerFromDBUseCase(customerID)
    }
}
