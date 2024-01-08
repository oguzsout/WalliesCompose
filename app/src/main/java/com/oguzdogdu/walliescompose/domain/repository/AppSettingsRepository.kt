package com.oguzdogdu.walliescompose.domain.repository

import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    suspend fun putThemeStrings(key:String, value:String)
    suspend fun getThemeStrings(key: String): Flow<String?>
    suspend fun putLanguageStrings(key:String, value:String)
    suspend fun getLanguageStrings(key: String):Flow<String?>
}