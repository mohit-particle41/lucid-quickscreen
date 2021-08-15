package com.lucidhearing.lucidquickscreen.domain.usecase

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.SaveCustomerMutation
import com.lucidhearing.lucidquickscreen.SourceTypesQuery
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.domain.repository.CustomerRepository

class SaveCustomerUseCase(private val customerRepository: CustomerRepository) {
    suspend fun execute(customer: Customer): Long {
        return customerRepository.saveCustomer(customer)
    }

    suspend fun sampleGraphQLRequest(): Response<SourceTypesQuery.Data>? {
        return customerRepository.sampleGetGraphQLRequest()
    }

    suspend fun syncCustomer(
        grandCentralCustomer: GrandCentralCustomer
    ): Response<SaveCustomerMutation.Data>? {
        return customerRepository.syncCustomer(grandCentralCustomer)
    }

    suspend fun getSyncCustomers():List<GrandCentralCustomer> {
        return customerRepository.getCustomerForSync()
    }
}