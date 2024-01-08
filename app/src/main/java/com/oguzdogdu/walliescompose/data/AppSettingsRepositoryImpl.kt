package com.oguzdogdu.walliescompose.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.domain.wrapper.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


private val Context.themeDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "THEME_KEYS"
)

private val Context.languageDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "language_preference"
)
class AppSettingsRepositoryImpl @Inject constructor(
    private val context: Context,
): AppSettingsRepository {
    override suspend fun putThemeStrings(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.themeDataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getThemeStrings(key: String): Flow<String?> {
        return flow {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.themeDataStore.data.first()
            emit(preference[preferencesKey])
        }
    }

    override suspend fun putLanguageStrings(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.languageDataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getLanguageStrings(key: String): Flow<String?> {
        return flow {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.languageDataStore.data.first()
            emit(preference[preferencesKey])
        }
    }
}