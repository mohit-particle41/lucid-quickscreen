package com.lucidhearing.lucidquickscreen.data.repository.dataSource

import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion

interface LifestyleQuestionLocalDataSource {
    suspend fun getLifestyleQuestions():List<LifestyleQuestion>
    suspend fun saveLifestyleQuestions(lifestyleQuestions: List<LifestyleQuestion>)
    suspend fun deleteAllLifestyleQuestions()
}