package com.r42914lg.arkados.yatodo.repository

import com.r42914lg.arkados.yatodo.YaTodoApp.Companion.REFRESH_INTERVAL
import com.r42914lg.arkados.yatodo.database.TodoDao
import com.r42914lg.arkados.yatodo.database.asDomainModel
import com.r42914lg.arkados.yatodo.database.asNetworkModel
import com.r42914lg.arkados.yatodo.log
import com.r42914lg.arkados.yatodo.model.*
import com.r42914lg.arkados.yatodo.network.TodoItemsContainer
import com.r42914lg.arkados.yatodo.network.TodoService
import com.r42914lg.arkados.yatodo.network.asDatabaseModel
import com.r42914lg.arkados.yatodo.repository.IRepo.Companion.CODE_FOR_EXCEPTION
import com.r42914lg.arkados.yatodo.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkResponseContainer(val items: MutableList<TodoItem>, val httpCode: Int)

interface IRepo {
    suspend fun getTodoList() : MutableList<TodoItem>
    suspend fun addOrUpdateTodo(todoItem: TodoItem)
    suspend fun deleteTodo(todoItem: TodoItem)
    suspend fun syncAll(calledByUser: Boolean) : Int
    suspend fun clearAllLocal()

    fun getFlow() : Flow<NetworkResponseContainer>

    companion object {
        const val CODE_FOR_EXCEPTION = 999
    }
}

class YaTodoRepo @Inject constructor(
    private val roomDao: TodoDao,
    private val networkService: TodoService,
    private val networkTracker: INetworkTracker,
    private val userManager: IUserManager,
    private val deviceIdManager: DeviceIdManager,
    ) : IRepo {

    override fun getFlow() = flowTodoItems
    private val flowTodoItems = flow {
        while (true) {
            if (networkTracker.isOnline.value == true && !userManager.token.isNullOrEmpty()) {
                log("emitting list items...")
                val httpCode = syncAll(false)
                emit(NetworkResponseContainer(getTodoList(), httpCode))
            }
            delay(REFRESH_INTERVAL)
        }
    }

    override suspend fun getTodoList() : MutableList<TodoItem> {
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

    override suspend fun syncAll(calledByUser:Boolean) : Int {
        val resultCode = withContext(Dispatchers.IO) {
            try {
                val response = networkService.updateAll(
                    TodoItemsContainer(
                        roomDao.getAllItems().asNetworkModel(),
                        deviceIdManager.deviceId))
                if (response.isSuccessful) {
                    response.body()?.let {
                        roomDao.deleteAll()
                        roomDao.insertAll(it.asDatabaseModel())
                    }
                } else {
                    log("Retrofit returned code --> ${response.code()}")
                }
                return@withContext response.code()
            } catch (e: Exception) {
                e.printStackTrace()
                e.message?.let { FirebaseHelper.logServerError(it) }
                return@withContext CODE_FOR_EXCEPTION
            }
        }

        log("syncAll ended with code --> $resultCode")
        return resultCode
    }

    override suspend fun clearAllLocal() {
        withContext(Dispatchers.IO) {
            roomDao.deleteAll()
        }
    }
}