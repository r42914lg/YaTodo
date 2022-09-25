package com.r42914lg.arkados.yatodo.graph

import android.app.Application
import com.r42914lg.arkados.yatodo.model.AuxVm
import com.r42914lg.arkados.yatodo.model.MainVm
import com.r42914lg.arkados.yatodo.model.DetailsVm
import com.r42914lg.arkados.yatodo.network.IOkHttpProvider
import com.r42914lg.arkados.yatodo.utils.IPermissionsHelper
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RepoModule::class,
    RoomModule::class,
    RetrofitModule::class,
    NetworkModule::class,
    UserModule::class,
    PermissionsModule::class,
    OkHttpModule::class,
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : AppComponent
    }

    fun getMainFactory(): MainVm.Factory
    fun getTodoDetailsFactory(): DetailsVm.Factory
    fun getAuxFactory(): AuxVm.Factory
    fun exposePermissionHelper(): IPermissionsHelper
    fun exposeOkHttpProvider(): IOkHttpProvider
}

@Singleton
@Component(modules = [
    RepoModule::class,
    RoomModule::class,
    RetrofitModule::class,
    NetworkModuleTest::class,
    UserModuleTest::class,
    PermissionsModuleTest::class,
    OkHttpModuleTest::class,
])
interface AppComponentTest : AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : AppComponentTest
    }
}

@Singleton
@Component(modules = [
    RepoModule::class,
    RoomModule::class,
    RetrofitModule::class,
    NetworkModule::class,
    UserModule::class,
    PermissionsModule::class,
    OkHttpModuleLocallySignedCert::class,
])
interface AppComponentLocalBackend : AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : AppComponentLocalBackend
    }
}