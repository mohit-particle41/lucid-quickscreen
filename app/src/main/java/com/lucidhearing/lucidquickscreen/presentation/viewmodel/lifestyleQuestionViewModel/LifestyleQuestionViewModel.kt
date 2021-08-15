package com.lucidhearing.lucidquickscreen.presentation.viewmodel.lifestyleQuestionViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.domain.usecase.DeleteAllLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.GetLocalLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.GetRetailerLifestyleQuestionsUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.SaveLifestyleQuestionsToDBUseCase
import com.lucidhearing.lucidquickscreen.presentation.util.listener.RequestStateListener
import com.lucidhearing.lucidquickscreen.utils.AppUtils
import com.lucidhearing.lucidquickscreen.utils.LifestyleQuestionUtil
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LifestyleQuestionViewModel(
    private val app: Application,
    private val getAllLifestyleQuestionsUseCase: GetLocalLifestyleQuestionsUseCase,
    private val saveLifestyleQuestionsToDBUseCase: SaveLifestyleQuestionsToDBUseCase,
    private val deleteAllLifestyleQuestionsUseCase: DeleteAllLifestyleQuestionsUseCase,
    private val getRetailerLifestyleQuestionsUseCase: GetRetailerLifestyleQuestionsUseCase
): AndroidViewModel(app) {
    var lifestyleQuestions: List<LifestyleQuestion> = listOf()
    var requestStateListener: RequestStateListener<List<LifestyleQuestion>>? = null
    var loadingState: MutableLiveData<Boolean> = MutableLiveData()

    fun saveLifestyleQuestions(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    loadingState.postValue(true)
                    //SampleData for creating local data for development untill APIs are available
                    val sampleQuestionData = listOf(
                        LifestyleQuestion(question = "Do you have trouble hearing in restaurants and other loud environments?",questionCode = "TROUBLE_HEARING_LOUD_ENV", createdAt = Date()),
                        LifestyleQuestion(question = "Do you have to turn the TV up to hear the dialog?",questionCode = "TURN_UP_TV", createdAt = Date()),
                        LifestyleQuestion(question = "Do you frequently ask people to repeat themselves?",questionCode = "ASK_PEOPLE_TO_REPEAT", createdAt = Date()),
                        LifestyleQuestion(question = "Do you listen to music for more than 1 hour a day?",questionCode = "MUSIC_MORE_THAN_1HR", createdAt = Date())
                    )
                    saveLifestyleQuestionsToDBUseCase.execute(sampleQuestionData)
                }catch (e:Exception){
                    loadingState.postValue(false)
                    Log.i(AppConstant.EXCEPTION_TAG,e.message.toString())
                    requestStateListener?.onError(getApplication<Application>().resources.getString(
                        R.string.error_unable_to_process))
                }
            }
        }
    }

    fun getLifestyleQuestions(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    loadingState.postValue(true)
                    delay(1000)
                    val response = getRetailerLifestyleQuestionsUseCase.execute()
                    val questions = response?.data?.getRetailerQuestions?.filterNotNull()
                    if(questions != null && !response.hasErrors()){
                        val retailerQuestions = LifestyleQuestionUtil.parseRetailerQuestions(questions)
                        deleteAllLifestyleQuestionsUseCase.execute()
                        saveLifestyleQuestionsToDBUseCase.execute(retailerQuestions)
                    }else{
                        deleteAllLifestyleQuestionsUseCase.execute()
                        saveLifestyleQuestions()
                    }
                    lifestyleQuestions = getAllLifestyleQuestionsUseCase.execute()
                    loadingState.postValue(false)
                    requestStateListener?.onSuccess(lifestyleQuestions)
                }catch (e:Exception){
                    loadingState.postValue(false)
                    Log.i(AppConstant.EXCEPTION_TAG,e.message.toString())
                    requestStateListener?.onError(getApplication<Application>().resources.getString(
                        R.string.error_unable_to_process))
                }
            }
        }
    }

    fun deleteLifestyleQuestions(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    deleteAllLifestyleQuestionsUseCase.execute()
                }catch (e:Exception){
                    Log.i(AppConstant.EXCEPTION_TAG,e.message.toString())

                }
            }
        }
    }

    fun clearLifestyleQuestionsData(){
        lifestyleQuestions = listOf()
    }
}