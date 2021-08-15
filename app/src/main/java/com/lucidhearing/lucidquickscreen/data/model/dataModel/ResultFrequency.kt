package com.lucidhearing.lucidquickscreen.data.model.dataModel

import java.io.Serializable

data class ResultFrequency(
    var frequency: String,
    var side:String,
    var l1:Int,
    var l2:Int,
    var dp:Int,
    var nf:Int,
    var snr:Int
):Serializable