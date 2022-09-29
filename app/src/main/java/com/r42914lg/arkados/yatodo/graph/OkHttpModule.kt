package com.r42914lg.arkados.yatodo.graph

import com.r42914lg.arkados.yatodo.network.IOkHttpProvider
import com.r42914lg.arkados.yatodo.network.OkHttpLocallySignedCert
import com.r42914lg.arkados.yatodo.network.OkHttpProd
import com.r42914lg.arkados.yatodo.network.OkHttpTestNoAuth
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface OkHttpModule {
    @Singleton
    @Binds
    fun getOkHttpClient(impl: OkHttpProd): IOkHttpProvider
}

@Module
interface OkHttpModuleTest {
    @Singleton
    @Binds
    fun getOkHttpClient(impl: OkHttpTestNoAuth): IOkHttpProvider
}

@Module
interface OkHttpModuleLocallySignedCert {
    @Singleton
    @Binds
    fun getOkHttpClient(impl: OkHttpLocallySignedCert): IOkHttpProvider
}