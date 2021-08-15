package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository

class GetCustomerListForSyncUseCase(private val customerRepository: CustomerRepository) {
    suspend fun execute():List<GrandCentralCustomer> {
        return customerRepository.getCustomerForSync()
    }
}