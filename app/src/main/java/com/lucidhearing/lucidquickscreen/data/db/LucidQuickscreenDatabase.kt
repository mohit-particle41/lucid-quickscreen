package com.lucidhearing.lucidquickscreen.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucidhearing.lucidquickscreen.data.model.entityModel.*
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant

@Database(
    entities = [
        Customer::class,
        LifestyleQuestion::class,
        CustomerLifestyleQuestionResponse::class,
        HearingTestResult::class,
        Retailer::class,
        RetailerProductRecommendation::class],
    version = AppConstant.DB_VERSION,
    exportSchema = false
)
@TypeConverters(DataTypeConverter::class)
abstract class LucidQuickscreenDatabase : RoomDatabase() {
    abstract fun customerDAO(): CustomerDAO
    abstract fun lifestyleQuestionDAO(): LifestyleQuestionDAO
    abstract fun hearingTestResultDAO(): HearingTestResultDAO
}