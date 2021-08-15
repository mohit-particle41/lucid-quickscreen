package com.lucidhearing.lucidquickscreen.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import com.google.common.truth.Truth.assertThat
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse

@RunWith(AndroidJUnit4::class)
class CustomerDAOTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: CustomerDAO
    private lateinit var database: LucidQuickscreenDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), LucidQuickscreenDatabase::class.java
        )
            .build()
        dao = database.customerDAO()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveCustomerTest() = runBlocking {
        val customer = Customer(
            id = 1,
            firstname = "User",
            lastname = "Test",
            email = "user@example.com",
            zip = "55555",
            phone = "5555555555",
            createdAt = Date(),
            isSynced = false
        )
        dao.saveCustomer(customer)
        val getCustomer = dao.getCustomerById(1)
        assertThat(getCustomer).isEqualTo(customer)
    }

    @Test
    fun saveCustomerQuestionResponses() = runBlocking {
        val responses = listOf(
            CustomerLifestyleQuestionResponse(
                id = 1,
                customerId = 1,
                questionCode = "TROUBLE_HEARING_LOUD_ENV",
                response = true,
                createdAt = Date()
            ),
            CustomerLifestyleQuestionResponse(
                id = 2,
                customerId = 1,
                questionCode = "ASK_PEOPLE_TO_REPEAT",
                response = false,
                createdAt = Date()
            ),
        )
        dao.saveCustomerQuestionResponses(responses)
        val getCustomerResponses = dao.getCustomerLifestyleQuestionResponse()
        assertThat(getCustomerResponses).isEqualTo(responses)
    }
}