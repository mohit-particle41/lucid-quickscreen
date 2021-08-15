package com.lucidhearing.lucidquickscreen.utils.syncService

import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import java.util.concurrent.TimeUnit

class CustomerDataSyncService(
    private val appContext:Context
) {
    private val workManager = WorkManager.getInstance(appContext)
    private val syncWorkerUniqueName = AppConstant.SYNC_WORKER_UNIQUE_NAME

    fun checkOrCreateDataSyncWorkRequest(){
        if (!checkWorkRequestActiveByUniqueName(syncWorkerUniqueName)){
            setupPeriodicWorkRequest()
        }
    }

    fun checkWorkRequestActiveByUniqueName(uniqueName:String):Boolean{
        val workQuery = WorkQuery.Builder
            .fromUniqueWorkNames(listOf(uniqueName))
            .addStates(listOf(WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING, WorkInfo.State.SUCCEEDED))
            .build()
        val workInfos: ListenableFuture<List<WorkInfo>> = workManager.getWorkInfos(workQuery)
        try {
            val workerList = workInfos.get()
            return workerList.size > 0
        }catch (e:Exception){
            Log.i(AppConstant.SYNC_WORKER_EXCEPTION_TAG, e.message.toString())
            return false
        }
    }

    fun setupPeriodicWorkRequest(){
        val periodicWorkRequest = PeriodicWorkRequest
            .Builder(SyncWorker::class.java,AppConstant.SYNC_WORKER_REPEAT_INTERVAL, TimeUnit.MINUTES)
            .addTag(syncWorkerUniqueName)
            .build()
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(syncWorkerUniqueName,
            ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest)
    }

    fun listAllWorkRequests(){
        val workQuery = WorkQuery.Builder
            .fromStates(listOf(WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING, WorkInfo.State.SUCCEEDED,WorkInfo.State.FAILED, WorkInfo.State.BLOCKED))
            .build()
        val workInfos: ListenableFuture<List<WorkInfo>> = workManager.getWorkInfos(workQuery)
        try {
            val workerList = workInfos.get()
        }catch (e:Exception){
            Log.i(AppConstant.SYNC_WORKER_EXCEPTION_TAG, e.message.toString())
        }
    }

    fun cancelAllWorkRequest(){
        try {
            workManager.cancelAllWork()
        }catch (e:Exception){
            Log.i(AppConstant.SYNC_WORKER_EXCEPTION_TAG, "Error while cancelling all workers" + e.message.toString())
        }
    }
}