package com.lucidhearing.lucidquickscreen.data.model.entityModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "RetailerProductRecommendation"
)
data class RetailerProductRecommendation(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String?,
    @ColumnInfo(name = "url")
    var url: String?,
    @ColumnInfo(name = "createdAt")
    var createdAt: Date?
)