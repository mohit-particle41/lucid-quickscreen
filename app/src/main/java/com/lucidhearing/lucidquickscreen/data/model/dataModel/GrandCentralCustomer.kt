package com.lucidhearing.lucidquickscreen.data.model.dataModel

import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.HearingTestResult

data class GrandCentralCustomer(
    var customer:Customer,
    var lifestyleQueResponse:List<CustomerLifestyleQuestionResponse>,
    var hearingTestResult: List<HearingTestResult>
)
