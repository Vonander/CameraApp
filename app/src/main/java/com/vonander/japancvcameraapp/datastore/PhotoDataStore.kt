package com.vonander.japancvcameraapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mainDataStore")

@Singleton
class PhotoDataStore {

    suspend fun setPhotoUriString(context: Context, newVaule: String) {
        context.dataStore.edit {
            it[URI_STRING_KEY] = newVaule
        }
    }

    suspend fun getPhotoUriString(context: Context): String {
        val valueFlow: Flow<String> = context.dataStore.data.map {
            it[URI_STRING_KEY]?: ""
        }

        return valueFlow.first()
    }

    companion object {
        private val URI_STRING_KEY = stringPreferencesKey("uri_string_key")
    }
}