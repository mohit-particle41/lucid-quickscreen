package com.lucidhearing.lucidquickscreen.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lucidhearing.lucidquickscreen.data.model.entityModel.HearingTestResult

@Dao
interface HearingTestResultDAO {
    @Insert
    suspend fun saveHearingTestResults(results:List<HearingTestResult>):List<Long>

    @Query("Delete from HearingTestResult where customerId=:customerId")
    suspend fun deleteCustomerHearingTestResults(customerId:Long)
}