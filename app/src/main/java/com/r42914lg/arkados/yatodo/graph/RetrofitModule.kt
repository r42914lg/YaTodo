package com.r42914lg.arkados.yatodo.graph

import com.r42914lg.arkados.yatodo.network.TodoNetwork
import com.r42914lg.arkados.yatodo.network.TodoService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun getNetworkService(todoNetwork: TodoNetwork) : TodoService =
        todoNetwork.service
}