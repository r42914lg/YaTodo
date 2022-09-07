package com.r42914lg.arkados.yatodo.graph

import android.app.Application
import androidx.room.Room
import com.r42914lg.arkados.yatodo.database.TodoDao
import com.r42914lg.arkados.yatodo.database.TodoDatabase
import com.r42914lg.arkados.yatodo.repository.IRepo
import com.r42914lg.arkados.yatodo.repository.YaTodoRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface RepoModule {
    @Singleton
    @Binds
    fun getRepo(impl: YaTodoRepo) : IRepo
}