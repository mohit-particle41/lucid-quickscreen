package com.lucidhearing.lucidquickscreen.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value:Long?):Date?{
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date:Date?):Long?{
        return date?.time?.toLong()
    }
}