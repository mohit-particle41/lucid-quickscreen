package com.lucidhearing.lucidquickscreen.data.prefDataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("retailer_preferences")

class RetailerPreferences(private val appContext: Context) {
        private val retailerDataStore = appContext.dataStore

        val retailerAPIKey: Flow<String?>
        get() = retailerDataStore.data.map { preferences ->
                preferences[RETAILER_API_KEY]
        }

        suspend fun saveRetailerAPIKey(key:String){
                retailerDataStore.edit { preferences ->
                        preferences[RETAILER_API_KEY] = key
                }
        }

        suspend fun removeRetailerAPIKey(){
                retailerDataStore.edit { preferences ->
                        preferences.remove(RETAILER_API_KEY)
                }
        }

        companion object{
                private val RETAILER_API_KEY = stringPreferencesKey("retailer_api_key")
        }
}