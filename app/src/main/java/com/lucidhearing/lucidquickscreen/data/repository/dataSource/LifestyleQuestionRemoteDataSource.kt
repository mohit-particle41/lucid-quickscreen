package com.lucidhearing.lucidquickscreen.data.repository.dataSource

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.GetRetailerQuestionsQuery

interface LifestyleQuestionRemoteDataSource {
    suspend fun getRetailerLifestyleQuestions(): Response<GetRetailerQuestionsQuery.Data>?
}