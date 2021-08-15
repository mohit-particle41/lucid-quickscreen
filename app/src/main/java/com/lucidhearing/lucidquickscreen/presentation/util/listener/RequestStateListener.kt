package com.lucidhearing.lucidquickscreen.presentation.util.listener

interface RequestStateListener<T> {
    fun onStarted(){}
    fun onSuccess(returnValue: T)
    fun onError(message:String)
}