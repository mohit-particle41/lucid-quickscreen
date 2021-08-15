package com.lucidhearing.lucidquickscreen.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class LifestyleQuestionDAOTest {
    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao: LifestyleQuestionDAO
    private lateinit var database: LucidQuickscreenDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), LucidQuickscreenDatabase::class.java
        )
            .build()
        dao = database.lifestyleQuestionDAO()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveLifestyleQuestionsTest() = runBlocking {
        val questionList = listOf(
            LifestyleQuestion(
                id = 1,
                question = "Do you have trouble hearing in restaurants and other loud environments?",
                questionCode = "TROUBLE_HEARING_LOUD_ENV",
                createdAt = Date()
            ),
            LifestyleQuestion(
                id = 2,
                question = "Do you frequently ask people to repeat themselves?",
                questionCode = "ASK_PEOPLE_TO_REPEAT",
                createdAt = Date()
            )
        )
        dao.saveLifestyleQuestions(questionList)
        val getLifestyleQuestions = dao.getLifestyleQuestions()
        Truth.assertThat(getLifestyleQuestions).isEqualTo(questionList)
    }

    @Test
    fun deleteAllLifestyleQuestionsTest()= runBlocking{
        val questionList = listOf(
            LifestyleQuestion(
                id = 1,
                question = "Do you have trouble hearing in restaurants and other loud environments?",
                questionCode = "TROUBLE_HEARING_LOUD_ENV",
                createdAt = Date()
            ),
            LifestyleQuestion(
                id = 2,
                question = "Do you frequently ask people to repeat themselves?",
                questionCode = "ASK_PEOPLE_TO_REPEAT",
                createdAt = Date()
            )
        )
        dao.saveLifestyleQuestions(questionList)
        dao.deleteAllLifestyleQuestions()
        val getLifestyleQuestions = dao.getLifestyleQuestions()
        Truth.assertThat(getLifestyleQuestions.size).isEqualTo(0)
    }
}