package com.lucidhearing.lucidquickscreen.data.db

import androidx.room.*
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithHearingTestResult
import com.lucidhearing.lucidquickscreen.data.model.entityModel.relation.CustomerWithLifestyleQuestionResponse

@Dao
interface CustomerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCustomer(customer: Customer): Long

    @Transaction
    @Query("Select * from Customer where id=:id")
    suspend fun getCustomerWithLifestyleQuestionResponses(
        id: Long
    ): CustomerWithLifestyleQuestionResponse

    @Query("Select * from Customer where id=:id")
    suspend fun getCustomerById(id: Int): Customer

    @Query("Select * from Customer")
    suspend fun getCustomerForSync(): List<Customer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCustomerQuestionResponses(
        customerLifestyleQuestionResponses: List<CustomerLifestyleQuestionResponse>
    ): List<Long>

    @Query("Select * from CustomerLifestyleQuestionResponse")
    suspend fun getCustomerLifestyleQuestionResponse(): List<CustomerLifestyleQuestionResponse>

    @Transaction
    @Query("Select * from Customer where id=:id")
    suspend fun getCustomerWithHearingTestResults(
        id: Long
    ): CustomerWithHearingTestResult

    @Query("Delete from Customer where id=:id")
    suspend fun deleteCustomerByID(id:Long)

    @Query("Delete from CustomerLifestyleQuestionResponse where customerId=:customerId")
    suspend fun deleteCustomerLifestyleQuestionResponse(customerId:Long)
}