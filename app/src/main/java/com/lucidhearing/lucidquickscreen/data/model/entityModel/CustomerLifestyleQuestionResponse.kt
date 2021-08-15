package com.lucidhearing.lucidquickscreen.data.model.entityModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "CustomerLifestyleQuestionResponse")
data class CustomerLifestyleQuestionResponse(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "customerId")
    var customerId: Long,
    @ColumnInfo(name = "questionCode")
    var questionCode: String?,
    @ColumnInfo(name = "response")
    var response: Boolean,
    @ColumnInfo(name = "createdAt")
    var createdAt: Date?
):Serializable