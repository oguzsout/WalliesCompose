package com.oguzdogdu.walliescompose.di

import com.oguzdogdu.walliescompose.util.ConnectivityManagerNetworkMonitor
import com.oguzdogdu.walliescompose.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}
