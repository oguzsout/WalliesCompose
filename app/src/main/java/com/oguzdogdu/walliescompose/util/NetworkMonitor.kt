package com.oguzdogdu.walliescompose.util

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow

@Stable
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}
