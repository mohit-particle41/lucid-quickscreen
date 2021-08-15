package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.domain.repository.LifestyleQuestionRepository

class DeleteAllLifestyleQuestionsUseCase(private val lifestyleQuestionRepository: LifestyleQuestionRepository) {
    suspend fun execute(){
        return lifestyleQuestionRepository.deleteAllLifestyleQuestions()
    }
}