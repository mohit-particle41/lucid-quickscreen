package com.lucidhearing.lucidquickscreen.data.model.entityModel.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.HearingTestResult

data class CustomerWithHearingTestResult(
    @Embedded val customer: Customer,
    @Relation(
        parentColumn = "id",
        entityColumn = "customerId"
    )
    val hearingTestResults: List<HearingTestResult>
)