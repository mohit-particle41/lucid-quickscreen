package com.lucidhearing.lucidquickscreen.presentation.viewmodel.customerViewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lucid.OAE.OAETest
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.SourceTypesQuery
import com.lucidhearing.lucidquickscreen.data.model.entityModel.Customer
import com.lucidhearing.lucidquickscreen.data.model.dataModel.CustomerDetails
import com.lucidhearing.lucidquickscreen.data.model.dataModel.QuestionResponse
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import com.lucidhearing.lucidquickscreen.data.model.dataModel.RetailerProduct
import com.lucidhearing.lucidquickscreen.data.util.Resource
import com.lucidhearing.lucidquickscreen.domain.usecase.*
import com.lucidhearing.lucidquickscreen.presentation.util.listener.RequestStateListener
import com.lucidhearing.lucidquickscreen.presentation.util.listener.SaveCustomerDetailsListener
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList

class CustomerViewModel(
    private val app: Application,
    private val saveCustomerUseCase: SaveCustomerUseCase,
    private val saveCustomerLifestyleQuestionResponseUseCase: SaveCustomerLifestyleQuestionResponseUseCase,
    private val getCustomerLifestyleQuestionResponseUseCase: GetCustomerLifestyleQuestionResponseUseCase,
    private val saveCustomerHearingTestResultUseCase: SaveCustomerHearingTestResultUseCase,
    private val getCustomerHearingTestResultUseCase: GetCustomerHearingTestResultsUseCase
) : AndroidViewModel(app) {
    val gqlResponseData: MutableLiveData<Resource<List<SourceTypesQuery.SourceType>?>> =
        MutableLiveData()
    var customer: CustomerDetails = CustomerDetails()
    var requestStateListener: RequestStateListener<Long>? = null
    var saveCustomerDetailsListener: SaveCustomerDetailsListener? = null
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()
    var lifestyleQuestionCustomerResponse = HashMap<String, QuestionResponse>()
    var currentCustomer: Long? = null
    var retailerProducts: List<RetailerProduct> = listOf()

    fun saveCustomer() {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                val customerDetails = Customer(
                    firstname = customer.firstname,
                    lastname = customer.lastname,
                    email = customer.email,
                    zip = customer.zip,
                    phone = customer.phone,
                    createdAt = Date()
                )
                val savedCustomer = saveCustomerUseCase.execute(customerDetails)
                savedCustomer.let {
                    loadingState.postValue(false)
                    currentCustomer = savedCustomer
                    requestStateListener?.onSuccess(savedCustomer)
                }
            } catch (e: Exception) {
                Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
                loadingState.postValue(false)
                currentCustomer = null
                requestStateListener?.onError(
                    getApplication<Application>().resources.getString(
                        R.string.error_unable_to_process
                    )
                )
            }
        }
    }

    fun sampleGraphQLRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            gqlResponseData.postValue(Resource.Loading())
            val response = saveCustomerUseCase.sampleGraphQLRequest()
            val launches = response?.data?.sourceTypes?.filterNotNull()
            if (launches != null && !response?.hasErrors()) {
                gqlResponseData.postValue(Resource.Success(launches))
            } else {
                gqlResponseData.postValue(
                    Resource.Error(
                        getApplication<Application>().resources.getString(
                            R.string.error_unable_to_process
                        )
                    )
                )
            }
        }
    }

    fun setQuestionResponse(questionKey: String, response: Boolean) {
        lifestyleQuestionCustomerResponse.put(questionKey, QuestionResponse(questionKey, response))
    }

    fun clearCustomerDetails() {
        currentCustomer = null
        customer = CustomerDetails()
        lifestyleQuestionCustomerResponse = HashMap<String, QuestionResponse>()
    }

    fun getProductRecommendation(){
        retailerProducts = listOf(
            RetailerProduct(R.drawable.aid1),
            RetailerProduct(R.drawable.aid2),
            RetailerProduct(R.drawable.aid3)
        )
    }

    fun saveTestResults(result:HashMap<String,ArrayList<ResultFrequency>>){
        viewModelScope.launch {
            try {
                currentCustomer?.let{ customerId ->
                    val savedResults = saveCustomerHearingTestResultUseCase.execute(customerId,result)
                    getCustomerTestResults()
                    saveCustomerDetailsListener?.onSaveCustomerDetailsSuccess()
                }
            }catch (e:Exception){
                Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
                saveCustomerDetailsListener?.onSaveCustomerDetailsError(e.message.toString())
            }
        }
    }

    fun getCustomerTestResults(){
        viewModelScope.launch {
            try {
                currentCustomer?.let { customerId ->
                    val results = getCustomerHearingTestResultUseCase.execute(customerId)
                    Log.i(AppConstant.DEBUG_TAG, Gson().toJson(results))
                }
            } catch (e: Exception) {
                Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
            }
        }
    }

    fun getCustomerLifestyleQuestionResponse(){
        viewModelScope.launch {
            try {
                currentCustomer?.let { customerId ->
                    val results = getCustomerLifestyleQuestionResponseUseCase.execute(customerId)
                    Log.i(AppConstant.DEBUG_TAG, Gson().toJson(results))
                }
            } catch (e: Exception) {
                Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
            }
        }
    }

    fun saveCustomerDetails(hearingTestResults:HashMap<String,ArrayList<ResultFrequency>>) {
        viewModelScope.launch {
            loadingState.postValue(true)
            delay(1500)
            try {
                val customerDetails = Customer(
                    firstname = customer.firstname,
                    lastname = customer.lastname,
                    email = customer.email,
                    zip = customer.zip,
                    phone = customer.phone,
                    createdAt = Date()
                )
                val savedCustomer = saveCustomerUseCase.execute(customerDetails)
                savedCustomer.let {
                    loadingState.postValue(false)
                    currentCustomer = savedCustomer
                    saveCustomerLifestyleQuestionResponseUseCase.execute(savedCustomer,lifestyleQuestionCustomerResponse)
                    saveCustomerHearingTestResultUseCase.execute(savedCustomer,hearingTestResults)
                    saveCustomerDetailsListener?.onSaveCustomerDetailsSuccess()
                }
            } catch (e: Exception) {
                Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
                loadingState.postValue(false)
                currentCustomer = null
                saveCustomerDetailsListener?.onSaveCustomerDetailsError(
                    getApplication<Application>().resources.getString(
                        R.string.error_unable_to_process
                    )
                )
            }
        }
    }

    fun getSyncCustomers(){
        viewModelScope.launch {
            try {
                val customers = saveCustomerUseCase.getSyncCustomers()
                if (customers.size > 0){
                    val response = saveCustomerUseCase.syncCustomer(customers[0])
                    val customerResponse = response?.data?.saveCustomer?.customer
                    if(customerResponse != null && !response.hasErrors()){
                        Log.i(AppConstant.DEBUG_TAG, response.toString())
                    }
                }
            }catch (e: Exception){
                Log.i(AppConstant.EXCEPTION_TAG, e.message.toString())
            }
        }
    }
}