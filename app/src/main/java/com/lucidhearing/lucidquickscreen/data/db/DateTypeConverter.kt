package com.lucidhearing.lucidquickscreen.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.lucidhearing.lucidquickscreen.data.model.dataModel.ResultFrequency
import java.util.*

class DataTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun listToJson(list:List<ResultFrequency>) =  Gson().toJson(list)

    @TypeConverter
    fun jsonTOList(value:String) = Gson().fromJson(value,Array<ResultFrequency>::class.java).toList()
}