package com.lucidhearing.lucidquickscreen.utils.syncService

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lucidhearing.lucidquickscreen.domain.usecase.DeleteCustomerFromDBUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.GetCustomerListForSyncUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.SaveCustomerUseCase
import com.lucidhearing.lucidquickscreen.domain.usecase.SyncCustomerUseCase
import com.lucidhearing.lucidquickscreen.utils.AppUtils
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val getCustomerListForSyncUseCase: GetCustomerListForSyncUseCase,
    val saveCustomerUseCase: SaveCustomerUseCase,
    val deleteCustomerFromDBUseCase: DeleteCustomerFromDBUseCase,
    val syncCustomerUseCase:SyncCustomerUseCase
) : CoroutineWorker(context, params) {
    private val TAG = "SyncWorkerLog"
    override suspend fun doWork(): Result {
        try {
            val customers = getCustomerListForSyncUseCase.execute()
            if(customers.size > 0 && AppUtils.isNetworkAvailable(applicationContext)) {
                customers.forEach { grandCentralCustomer ->
                    val customerID = grandCentralCustomer.customer.id!!
                    val response = syncCustomerUseCase.execute(grandCentralCustomer)
                    val customer = response?.data?.saveCustomer?.customer
                    if (customer != null && !response!!.hasErrors()) {
                        Log.i(TAG,"Sync Customer response for local user ${customerID}: New customer " + customer.id + " Uni Id: " + customer.universalID)
                        //deleteCustomerFromDBUseCase.execute(customerID)
                    }else{
                        Log.i(TAG, "Sync Request failed for customer " + customerID)
                    }
                }
            }
            return Result.success()
        } catch (e: Exception) {
            Log.i(AppConstant.SYNC_WORKER_EXCEPTION_TAG, "Error in Worker ${e.message.toString()} ")
            return Result.failure()
        }
    }
}
