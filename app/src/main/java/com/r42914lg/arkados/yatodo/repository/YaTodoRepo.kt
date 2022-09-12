package com.r42914lg.arkados.yatodo.repository

import com.r42914lg.arkados.yatodo.database.TodoDao
import com.r42914lg.arkados.yatodo.database.asDomainModel
import com.r42914lg.arkados.yatodo.database.asNetworkModel
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.TodoItem
import com.r42914lg.arkados.yatodo.model.asDatabaseModel
import com.r42914lg.arkados.yatodo.network.TodoService
import com.r42914lg.arkados.yatodo.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IRepo {
    suspend fun getTodoList() : List<TodoItem>
    suspend fun addOrUpdateTodo(todoItem: TodoItem)
    suspend fun deleteTodo(todoItem: TodoItem)
    suspend fun syncAll()
    suspend fun clearAllLocal()
}

class YaTodoRepo @Inject constructor(
    private val roomDao: TodoDao,
    private val networkService: TodoService
    ) : IRepo {

    override suspend fun getTodoList() : List<TodoItem> {
        return withContext(Dispatchers.IO) {
            roomDao.getNoDelItems().asDomainModel()
        }
    }

    override suspend fun addOrUpdateTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            if (todoItem.id != null && roomDao.isRecordExist(todoItem.id!!))
                roomDao.updateItem(todoItem.asDatabaseModel())
            else
                roomDao.insertItem(todoItem.asDatabaseModel())
        }
    }

    override suspend fun deleteTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            roomDao.deleteItem(todoItem.asDatabaseModel())
        }
    }

    override suspend fun syncAll() {
        withContext(Dispatchers.IO) {
            try {
                val response = networkService.updateAll(roomDao.getAllItems().asNetworkModel())
                if (response.isSuccessful) {
                    response.body()?.let {
                        roomDao.deleteAll()
                        roomDao.insertAll(it.asDatabaseModel())
                    }
                } else {
                    log("Retrofit returned code --> ${response.code()}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun clearAllLocal() {
        withContext(Dispatchers.IO) {
            roomDao.deleteAll()
        }
    }
}