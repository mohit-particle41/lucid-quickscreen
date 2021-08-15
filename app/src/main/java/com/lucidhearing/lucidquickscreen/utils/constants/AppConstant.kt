package com.lucidhearing.lucidquickscreen.utils.constants

import com.lucidhearing.lucidquickscreen.BuildConfig

object AppConstant {
    const val DB_NAME = "lucid_quickscreen_db"
    const val DB_VERSION = 3
    const val GRAPHQL_BASE_URL = "https://graphql-api.azure-api.net/graphql"
    const val SYNC_WORKER_UNIQUE_NAME = "CustomerSyncPeriodicWork"
    const val SYNC_WORKER_EXCEPTION_TAG = "SyncWorker"
    const val SYNC_WORKER_REPEAT_INTERVAL:Long = 16
    const val EXCEPTION_TAG = "LQException"
    const val DEBUG_TAG = "LQDebug"
    const val NAME_MIN_CHAR_LENGTH = 2
    const val APP_CENTER_SDK_KEY = BuildConfig.APP_CENTER_SDK_KEY
    const val ESPER_SDK_KEY = BuildConfig.ESPER_SDK_KEY
    const val APIM_SUBSCRIPTION_KEY_HEADER = "Ocp-Apim-Subscription-Key"

    //USB
    val INTENT_ACTION_GRANT_USB: String = BuildConfig.APPLICATION_ID + ".GRANT_USB"
    const val INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect"
    const val NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel"
    const val INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity"

    //WIFI
    const val SECURITY_CAPABILITY_WAP = "WPA"
    const val SECURITY_CAPABILITY_WEP = "WEP"
    const val UNNAMED_NETWORK = "<Unnamed Network>"
    const val WIFI_PASS_MIN_LENGTH = 8

    //App Version
    const val APP_VERSION_CODE = BuildConfig.VERSION_CODE
    const val APP_VERSION_NAME = BuildConfig.VERSION_NAME

    // values have to be unique within each app
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001
}