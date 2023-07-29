package com.example.privatealbum.password

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

val Context.preferenceDataStore:DataStore<Preferences> by preferencesDataStore("password_dataStore")
private val passwordKey = stringPreferencesKey("password")

//保存密码
fun savePassword(context: Context,password:String,scope: CoroutineScope){
    scope.launch(Dispatchers.IO){
        context.preferenceDataStore.edit {
            it[passwordKey] = password
        }
    }
}

//提取密码
fun getPassword(context: Context,scope: CoroutineScope,callBack:(String?)->Unit){
    scope.launch(Dispatchers.IO){
        val passwordPreferences = context.preferenceDataStore.data.first {
            true
        }
        withContext(Dispatchers.Main){
            callBack(passwordPreferences[passwordKey])
        }
    }
}







