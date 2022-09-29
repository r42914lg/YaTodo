package com.r42914lg.arkados.yatodo.graph

import com.r42914lg.arkados.yatodo.utils.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PermissionsModule {
    @Singleton
    @Binds
    fun getPermissionHelper(impl: PermissionsHelper): IPermissionsHelper
}

@Module
interface PermissionsModuleTest {
    @Singleton
    @Binds
    fun getPermissionHelper(impl: PermissionsHelperTest): IPermissionsHelper
}