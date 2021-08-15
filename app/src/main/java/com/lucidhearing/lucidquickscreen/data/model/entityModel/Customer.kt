package com.lucidhearing.lucidquickscreen.data.model.entityModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "Customer"
)
data class Customer(
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null,
    @ColumnInfo(name = "firstname")
    var firstname:String?,
    @ColumnInfo(name = "lastname")
    var lastname:String?,
    @ColumnInfo(name = "email")
    var email:String?,
    @ColumnInfo(name = "zip")
    var zip:String?,
    @ColumnInfo(name = "phone")
    var phone:String?,
    @ColumnInfo(name = "isAborted")
    var isAborted:Boolean?=false,
    @ColumnInfo(name = "abortStage")
    var abortStage:String?=null,
    @ColumnInfo(name = "createdAt")
    var createdAt:Date?,
    @ColumnInfo(name = "isSynced")
    var isSynced:Boolean?=false
):Serializable {
}