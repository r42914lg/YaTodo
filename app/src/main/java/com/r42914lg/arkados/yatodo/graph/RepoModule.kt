package com.r42914lg.arkados.yatodo.graph

import com.r42914lg.arkados.yatodo.repository.IRepo
import com.r42914lg.arkados.yatodo.repository.YaTodoRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepoModule {
    @Singleton
    @Binds
    fun getRepo(impl: YaTodoRepo) : IRepo
}