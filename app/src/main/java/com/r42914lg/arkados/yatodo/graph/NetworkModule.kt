package com.r42914lg.arkados.yatodo.graph

import com.r42914lg.arkados.yatodo.utils.INetworkTracker
import com.r42914lg.arkados.yatodo.utils.NetworkTracker
import com.r42914lg.arkados.yatodo.utils.NetworkTrackerTest
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NetworkModule {
    @Singleton
    @Binds
    fun getNetworkTracker(impl: NetworkTracker): INetworkTracker
}

@Module
interface NetworkModuleTest {
    @Singleton
    @Binds
    fun getNetworkTracker(impl: NetworkTrackerTest): INetworkTracker
}