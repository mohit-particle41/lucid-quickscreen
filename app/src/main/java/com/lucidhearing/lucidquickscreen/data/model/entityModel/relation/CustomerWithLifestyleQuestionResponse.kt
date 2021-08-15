package com.lucidhearing.lucidquickscreen.data.model.entityModel.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse

data class CustomerWithLifestyleQuestionResponse (
    @Embedded val customer:Customer,
    @Relation(
        parentColumn = "id",
        entityColumn = "customerId"
    )
    val questionResponses:List<CustomerLifestyleQuestionResponse>
)