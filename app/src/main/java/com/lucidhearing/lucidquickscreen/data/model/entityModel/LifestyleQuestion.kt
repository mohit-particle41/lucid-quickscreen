package com.lucidhearing.lucidquickscreen.data.model.entityModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "LifestyleQuestion")
data class LifestyleQuestion(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    @ColumnInfo(name = "question")
    var question:String?,
    @ColumnInfo(name = "questionCode")
    var questionCode:String?,
    @ColumnInfo(name = "createdAt")
    var createdAt: Date?
) {
}