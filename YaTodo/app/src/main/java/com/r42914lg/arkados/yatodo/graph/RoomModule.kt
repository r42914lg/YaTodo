package com.r42914lg.arkados.yatodo.graph

import android.app.Application
import androidx.room.Room
import com.r42914lg.arkados.yatodo.database.TodoDao
import com.r42914lg.arkados.yatodo.database.TodoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    fun getRoomDao(app: Application) : TodoDao {
        return Room.databaseBuilder(app, TodoDatabase::class.java, "todo_db")
            .build()
            .todoDao
    }
}