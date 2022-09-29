package com.r42914lg.arkados.yatodo.graph

import com.r42914lg.arkados.yatodo.utils.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UserModule {
    @Singleton
    @Binds
    fun getUserManager(impl: UserManager): IUserManager
}

@Module
interface UserModuleTest {
    @Singleton
    @Binds
    fun getUserManager(impl: UserManagerTest): IUserManager
}