package com.r42914lg.arkados.yatodo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.r42914lg.arkados.yatodo.database.TodoDao
import com.r42914lg.arkados.yatodo.database.TodoDatabase
import com.r42914lg.arkados.yatodo.database.asDomainModel
import com.r42914lg.arkados.yatodo.model.Importance
import com.r42914lg.arkados.yatodo.model.TodoItem
import com.r42914lg.arkados.yatodo.model.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IRepo {
    suspend fun getTodoList() : List<TodoItem>
    suspend fun addTodo(todoItem: TodoItem)
    suspend fun updateTodo(todoItem: TodoItem)
    suspend fun deleteTodo(todoItem: TodoItem)
}

class YaTodoRepo @Inject constructor(private val dao: TodoDao) : IRepo {

    override suspend fun getTodoList() : List<TodoItem> {
        return withContext(Dispatchers.IO) {
            dao.getNoDelItems().asDomainModel()
        }
    }

    override suspend fun addTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            dao.insertItem(todoItem.asDatabaseModel())
        }
    }

    override suspend fun updateTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            dao.updateItem(todoItem.asDatabaseModel())
        }
    }

    override suspend fun deleteTodo(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            dao.deleteItem(todoItem.asDatabaseModel())
        }
    }
}