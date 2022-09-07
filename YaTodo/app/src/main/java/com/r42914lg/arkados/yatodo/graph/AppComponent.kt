package com.r42914lg.arkados.yatodo.graph

import android.app.Application
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.DetailsVm
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepoModule::class, RoomModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : AppComponent
    }

    fun getMainFactory(): MainVm.Factory
    fun getTodoDetailsFactory(): DetailsVm.Factory
}