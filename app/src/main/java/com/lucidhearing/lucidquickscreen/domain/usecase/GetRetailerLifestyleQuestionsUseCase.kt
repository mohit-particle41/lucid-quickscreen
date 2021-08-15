package com.lucidhearing.lucidquickscreen.domain.usecase

import com.apollographql.apollo.api.Response
import com.lucidhearing.lucidquickscreen.GetRetailerQuestionsQuery
import com.lucidhearing.lucidquickscreen.domain.repository.LifestyleQuestionRepository

class GetRetailerLifestyleQuestionsUseCase(
    private val lifestyleQuestionRepository: LifestyleQuestionRepository
) {
    suspend fun execute(): Response<GetRetailerQuestionsQuery.Data>?{
        return lifestyleQuestionRepository.getRetailerLifestyleQuestions()
    }
}