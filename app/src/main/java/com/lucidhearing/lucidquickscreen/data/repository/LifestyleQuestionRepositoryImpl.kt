package com.lucidhearing.lucidquickscreen.data.repository

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.GetRetailerQuestionsQuery
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionLocalDataSource
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionRemoteDataSource
import com.lucidhearing.lucidquickscreen.domain.repository.LifestyleQuestionRepository

class LifestyleQuestionRepositoryImpl(
    private val lifestyleQuestionLocalDataSource: LifestyleQuestionLocalDataSource,
    private val lifestyleQuestionRemoteDataSource: LifestyleQuestionRemoteDataSource
):LifestyleQuestionRepository {
    override suspend fun getRetailerLifestyleQuestions(): Response<GetRetailerQuestionsQuery.Data>? {
        return lifestyleQuestionRemoteDataSource.getRetailerLifestyleQuestions()
    }

    override suspend fun getLocalLifestyleQuestions(): List<LifestyleQuestion> {
        return lifestyleQuestionLocalDataSource.getLifestyleQuestions()
    }

    override suspend fun saveLifestyleQuestionsToDB(lifestyleQuestions: List<LifestyleQuestion>) {
        lifestyleQuestionLocalDataSource.saveLifestyleQuestions(lifestyleQuestions)
    }

    override suspend fun deleteAllLifestyleQuestions() {
        lifestyleQuestionLocalDataSource.deleteAllLifestyleQuestions()
    }
}