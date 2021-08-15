package com.lucidhearing.lucidquickscreen.domain.usecase

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.SaveCustomerMutation
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository

class SyncCustomerUseCase(private val customerRepository: CustomerRepository) {
    suspend fun execute(
        grandCentralCustomer: GrandCentralCustomer
    ): Response<SaveCustomerMutation.Data>? {
        return customerRepository.syncCustomer(grandCentralCustomer)
    }
}