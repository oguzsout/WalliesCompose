package com.oguzdogdu.walliescompose

import com.oguzdogdu.walliescompose.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestNetworkMonitor : NetworkMonitor {

    private val connectivityFlow = MutableStateFlow(true)

    override val isOnline: Flow<Boolean> = connectivityFlow

    fun setConnected(isConnected: Boolean) {
        connectivityFlow.value = isConnected
    }
}
