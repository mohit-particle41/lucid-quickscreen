package com.lucidhearing.lucidquickscreen.data.model.entityModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "Retailer"
)
data class Retailer(
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null,
    @ColumnInfo(name = "attribute")
    var attribute:String?,
    @ColumnInfo(name = "value")
    var value:String?,
    @ColumnInfo(name = "createdAt")
    var createdAt:Date?
)
