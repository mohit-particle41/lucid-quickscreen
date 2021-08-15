package com.lucidhearing.lucidquickscreen.presentation.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lucidhearing.lucidquickscreen.data.db.CustomerDAO
import com.lucidhearing.lucidquickscreen.data.db.HearingTestResultDAO
import com.lucidhearing.lucidquickscreen.data.db.LifestyleQuestionDAO
import com.lucidhearing.lucidquickscreen.data.db.LucidQuickscreenDatabase
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideLucidQuickscreenDatabase(app:Application):LucidQuickscreenDatabase{
        //Sample Migration object
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Customer ADD COLUMN isSynced INTEGER")
            }
        }

        return Room.databaseBuilder(app,LucidQuickscreenDatabase::class.java,AppConstant.DB_NAME)
//            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomerDAO(lucidQuickscreenDatabase: LucidQuickscreenDatabase):CustomerDAO{
        return lucidQuickscreenDatabase.customerDAO()
    }

    @Provides
    @Singleton
    fun provideLifestyleQuestionDAO(lucidQuickscreenDatabase: LucidQuickscreenDatabase):LifestyleQuestionDAO{
        return lucidQuickscreenDatabase.lifestyleQuestionDAO()
    }

    @Provides
    @Singleton
    fun provideHearingTestResultDAO(lucidQuickscreenDatabase: LucidQuickscreenDatabase):HearingTestResultDAO{
        return lucidQuickscreenDatabase.hearingTestResultDAO()
    }
}