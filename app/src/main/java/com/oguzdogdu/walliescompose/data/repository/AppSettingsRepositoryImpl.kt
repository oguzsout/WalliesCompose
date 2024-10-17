package com.oguzdogdu.walliescompose.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl.Companion.HOME_IMAGE_ROTATE_KEY
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl.Companion.LANGUAGE_KEY
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl.Companion.THEME_KEY
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.util.ThemeKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = THEME_KEY
)

private val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(
    name = LANGUAGE_KEY
)
private val Context.homeRotateImageCardVisibility: DataStore<Preferences> by preferencesDataStore(
    name = HOME_IMAGE_ROTATE_KEY
)

class AppSettingsRepositoryImpl @Inject constructor(
    private val context: Context,
) : AppSettingsRepository {
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

    override suspend fun putHomeRotateCardVisibility(key: String, value: Boolean?) {
        val preferencesKey = booleanPreferencesKey(key)
        context.homeRotateImageCardVisibility.edit {
            if (value == null) it[preferencesKey] = true
            else it[preferencesKey] = value
        }
    }

    override fun getHomeRotateCardVisibility(key: String): Flow<Boolean> {
        return flow {
            val preferencesKey = booleanPreferencesKey(key)
            val preference = context.homeRotateImageCardVisibility.data.first()
            preference[preferencesKey]?.let { emit(it) }
        }
    }
    companion object {
         const val THEME_KEY = "THEME_KEY"
         const val LANGUAGE_KEY = "LANGUAGE_KEY"
         const val HOME_IMAGE_ROTATE_KEY = "HOME_IMAGE_ROTATE_KEY"
    }
}