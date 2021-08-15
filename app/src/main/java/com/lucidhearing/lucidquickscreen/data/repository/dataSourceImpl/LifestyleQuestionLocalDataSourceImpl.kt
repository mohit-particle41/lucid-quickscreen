package com.lucidhearing.lucidquickscreen.data.repository.dataSourceImpl

import com.lucidhearing.lucidquickscreen.data.db.LifestyleQuestionDAO
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import com.lucidhearing.lucidquickscreen.data.repository.dataSource.LifestyleQuestionLocalDataSource

class LifestyleQuestionLocalDataSourceImpl(
    private val lifestyleQuestionDAO: LifestyleQuestionDAO
    ):LifestyleQuestionLocalDataSource {
    override suspend fun getLifestyleQuestions(): List<LifestyleQuestion> {
        return lifestyleQuestionDAO.getLifestyleQuestions()
    }

    override suspend fun saveLifestyleQuestions(lifestyleQuestions: List<LifestyleQuestion>) {
        return lifestyleQuestionDAO.saveLifestyleQuestions(lifestyleQuestions)
    }

    override suspend fun deleteAllLifestyleQuestions() {
        return lifestyleQuestionDAO.deleteAllLifestyleQuestions()
    }
}