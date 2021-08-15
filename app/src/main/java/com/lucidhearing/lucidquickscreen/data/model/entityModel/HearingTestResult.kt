package com.lucidhearing.lucidquickscreen.data.model.entityModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import java.io.Serializable
import java.util.*

@Entity(tableName = "HearingTestResult")
data class HearingTestResult(
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null,
    @ColumnInfo(name = "customerId")
    var customerId:Long,
    @ColumnInfo(name = "side")
    var side:String,
    @ColumnInfo(name = "hearingLossLevel")
    var hearingLossLevel: String,
    @ColumnInfo(name = "frequency")
    var frequency:List<ResultFrequency>,
    @ColumnInfo(name = "createdDate")
    var createdDate: Date
):Serializable