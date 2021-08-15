package com.lucidhearing.lucidquickscreen.domain.repository

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.GetRetailerQuestionsQuery
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion

interface LifestyleQuestionRepository {
    suspend fun getRetailerLifestyleQuestions(): Response<GetRetailerQuestionsQuery.Data>?
    suspend fun getLocalLifestyleQuestions():List<LifestyleQuestion>
    suspend fun saveLifestyleQuestionsToDB(lifestyleQuestions: List<LifestyleQuestion>)
    suspend fun deleteAllLifestyleQuestions()
}