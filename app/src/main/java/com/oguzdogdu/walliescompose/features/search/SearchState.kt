package com.oguzdogdu.walliescompose.features.search

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.userpreferences.UserPreferences

@Immutable
data class SearchState(
    val speechDialogState: Boolean = false,
    val userPreferences: List<UserPreferences> = emptyList()
)
