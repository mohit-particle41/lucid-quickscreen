package com.lucidhearing.lucidquickscreen.data.db

import androidx.room.*
import com.lucidhearing.lucidquickscreen.data.model.entityModel.LifestyleQuestion

@Dao
interface LifestyleQuestionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLifestyleQuestions(lifestyleQuestions: List<LifestyleQuestion>)

    @Query("Select * from LifestyleQuestion")
    suspend fun getLifestyleQuestions():List<LifestyleQuestion>

    @Query("Delete from LifestyleQuestion")
    suspend fun deleteAllLifestyleQuestions()
}