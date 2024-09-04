package com.oguzdogdu.walliescompose.domain.model.userpreferences

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class UserPreferences(
    val id: Int? = null,
    val keyword: String? = null
)
