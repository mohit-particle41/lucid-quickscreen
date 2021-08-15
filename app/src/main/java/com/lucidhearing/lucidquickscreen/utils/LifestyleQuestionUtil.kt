package com.lucidhearing.lucidquickscreen.utils

import com.lucidhearing.lucidquickscreen.GetRetailerQuestionsQuery
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import java.util.*

object LifestyleQuestionUtil {
    fun parseRetailerQuestions(
        questions: List<GetRetailerQuestionsQuery.GetRetailerQuestion>
    ):List<LifestyleQuestion>{
        val questionList = mutableListOf<LifestyleQuestion>()
        questions.forEach { question ->
            questionList.add(LifestyleQuestion(
                question = question.questionTextPhrase[0].value,
                questionCode = question.code,
                createdAt = Date()
            ))
        }
        return questionList
    }
}