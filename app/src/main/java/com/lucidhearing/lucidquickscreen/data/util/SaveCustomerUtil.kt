package com.lucidhearing.lucidquickscreen.data.util

import com.apollographql.apollo.api.Input
import com.google.gson.Gson
import com.lucidhearing.lucidquickscreen.data.model.dataModel.GrandCentralCustomer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.entityModel.CustomerLifestyleQuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.entityModel.HearingTestResult
import com.lucidhearing.lucidquickscreen.type.*
import com.lucidhearing.lucidquickscreen.utils.constants.GrandCentralCodeConstant
import java.util.*

object SaveCustomerUtil {
    fun prepareSaveCustomerMutationRequest(grandCentralCustomer: GrandCentralCustomer):Input<CustomerInput>{
        val customerDetails = grandCentralCustomer.customer
        val hearingTestDetails = grandCentralCustomer.hearingTestResult
        val lifestyleQuestionResponse = grandCentralCustomer.lifestyleQueResponse
        val customerInput:CustomerInput = CustomerInput(
            firstName = getInputValue(customerDetails.firstname ?: ""),
            lastName = getInputValue(customerDetails.lastname ?: ""),
            sourceCode = getInputValue("LQT"),
            contactInformation = prepareCustomerContactDetails(customerDetails),
            addresses = prepareCustomerAddressDetails(customerDetails),
            touchpoint = prepareCustomerTouchPointDetails(hearingTestDetails),
            lifestyleAttribute = prepareCustomerLifestyleAttributeDetails(lifestyleQuestionResponse)
        )
        return getInputValue(customerInput)
    }

    fun prepareCustomerContactDetails(customer:Customer):
            Input<ContactInput>{
        val emailContactInfoInput = ContactInfoInput(
            contact = getInputValue(customer.email ?: ""),
            typeCode = getInputValue(GrandCentralCodeConstant.CONTACT_TYPE_EMAIL)
        )
        val phoneContactInfoInput = ContactInfoInput(
            contact = getInputValue(customer.phone ?: ""),
            typeCode = getInputValue(GrandCentralCodeConstant.CONTACT_TYPE_PHONE)
        )
        return getInputValue(
            ContactInput(
                contactInfo = getInputValue(listOf(emailContactInfoInput,phoneContactInfoInput))
            )
        )
    }

    fun prepareCustomerAddressDetails(customer:Customer):Input<List<AddressInput?>>{
        val addressInput: AddressInput = AddressInput(
            zIP = getInputValue(customer.zip ?: "")
        )
        return getInputValue(listOf(addressInput))
    }

    fun prepareCustomerTouchPointDetails(hearingTestResult: List<HearingTestResult>): Input<TouchpointInput>{
        return getInputValue(
            TouchpointInput(
                activity = getInputValue(GrandCentralCodeConstant.ACTIVITY_HEARING_TEST),
                hearingTest = if(hearingTestResult.size > 0) prepareHearingTestDetails(hearingTestResult)
                else Input.optional(null)
            )
        )
    }

    fun prepareHearingTestDetails(hearingTestResult: List<HearingTestResult>): Input<HearingTestInput>{
        return getInputValue(
            HearingTestInput(
                hearingTestTypeCode = getInputValue(GrandCentralCodeConstant.HEARING_TEST_TYPE),
                leftSideLossLevelCode = getInputValue(getLossLevelCode(GrandCentralCodeConstant.LEFT_SIDE,hearingTestResult)),
                rightSideLossLevelCode = getInputValue(getLossLevelCode(GrandCentralCodeConstant.RIGHT_SIDE,hearingTestResult)),
                hearingTestDate = hearingTestResult.get(0).createdDate,
                confidence = 0,
                rawResults = getInputValue(Gson().toJson(hearingTestResult))
            )
        )
    }

    fun getLossLevelCode(side:String,hearingTestResult:List<HearingTestResult>):String{
        hearingTestResult.forEach { result ->
            if(result.side == side){
                return result.hearingLossLevel
            }
        }
        return ""
    }

    fun prepareCustomerLifestyleAttributeDetails(responses:List<CustomerLifestyleQuestionResponse>):Input<LifestyleAttributeInput>{
        return getInputValue(
            LifestyleAttributeInput(
                commitment = false,
                lifestyleAttrQuesResponses = getInputValue(getLifestyleAttributeQuestionResponse(responses))
            )
        )
    }

    fun getLifestyleAttributeQuestionResponse(responses:List<CustomerLifestyleQuestionResponse>):List<LifestyleAttrQuesRespInput?>{
        val customerResponse =  mutableListOf<LifestyleAttrQuesRespInput?>()
        responses.forEach { response ->
            customerResponse.add(
                LifestyleAttrQuesRespInput(
                    questionCode = getInputValue(response.questionCode ?: ""),
                    answer = getInputValue(getQuestionResponse(response.response))
                )
            )
        }
        return customerResponse
    }

    fun getQuestionResponse(response:Boolean):String{
        return if(response) GrandCentralCodeConstant.QUE_RESPONSE_YES else GrandCentralCodeConstant.QUE_RESPONSE_NO
    }

    fun <T>getInputValue(value:T):Input<T>{
        return Input.optional(value)
    }
}