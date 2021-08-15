package com.lucidhearing.lucidquickscreen.domain.usecase

import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.domain.repository.LifestyleQuestionRepository

class SaveLifestyleQuestionsToDBUseCase(private val lifestyleQuestionRepository: LifestyleQuestionRepository) {
    suspend fun execute(lifestyleQuestions: List<LifestyleQuestion>){
        return lifestyleQuestionRepository.saveLifestyleQuestionsToDB(lifestyleQuestions)
    }
}