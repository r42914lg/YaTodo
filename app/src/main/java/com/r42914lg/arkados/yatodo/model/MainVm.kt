package com.r42914lg.arkados.yatodo.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.r42914lg.arkados.yatodo.R
import com.r42914lg.arkados.yatodo.network.TokenManager
import com.r42914lg.arkados.yatodo.repository.IRepo
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.*

class MainVm @AssistedInject constructor(
    private val app: Application,
    private val repo: IRepo,
    private val tokenManager: TokenManager
) : AndroidViewModel(app) {

    @AssistedFactory
    interface Factory {
        fun create(): MainVm
    }

    private var _toastToUi = MutableLiveData<String?>()
    fun toastShown() { _toastToUi.value = null }
    val toastUi: LiveData<String?>
        get() = _toastToUi

    val currentUser: LiveData<String?>
        get() = tokenManager.userName

    private val _showCompleted = MutableLiveData(true)
    val showCompleted: LiveData<Boolean>
        get() = _showCompleted

    fun setShowCompleted(value: Boolean) {
        _showCompleted.value = value
        prepareList()
    }

    private val _todoItems = MutableLiveData<List<TodoItem>>()
    val todoItems: LiveData<List<TodoItem>>
        get() = _todoItems

    private val _countCompleted = MutableLiveData(todoItems.value?.countCompleted() ?: 0)
    val countCompleted: LiveData<Int>
        get() = _countCompleted


    private var _eventLoginRequest = MutableLiveData<Boolean>(false)
    val eventLoginRequest: LiveData<Boolean>
        get() = _eventLoginRequest

    private var _eventLogoutRequest = MutableLiveData<Boolean>(false)
    val evenLogoutRequest: LiveData<Boolean>
        get() = _eventLogoutRequest

    init {
        prepareList()
    }

    fun prepareList() {
        viewModelScope.launch {
            val todoItemsFromRepo = repo.getTodoList()
            _countCompleted.value = todoItemsFromRepo.countCompleted()
            _todoItems.value =
                if (showCompleted.value == false) {
                    todoItemsFromRepo.filterNonCompletedOnly()
                } else
                    todoItemsFromRepo
        }
    }

    fun onDelete(item: TodoItem) {
        item.deletepending = true
        item.changed = Calendar.getInstance().time
        viewModelScope.launch {
            repo.addOrUpdateTodo(item)
        }
    }

    fun onCompleteChanged(item: TodoItem) {
        item.changed = Calendar.getInstance().time
        viewModelScope.launch {
            repo.addOrUpdateTodo(item)
        }
    }

    fun handleSyncRequest() {
        viewModelScope.launch {
            repo.syncAll()
        }
        prepareList()
    }

    fun onSignSuccess(userName: String, token: String) {
        _eventLoginRequest.value = false
        tokenManager.saveAuthToken(userName, token)
    }

    fun onSignFailure() {
        _eventLoginRequest.value = false
    }

    fun handleLoginOrLogoutClick() {
        if (currentUser.value == null) {
            _eventLoginRequest.value = true
        } else {
            _toastToUi.value = app.getString(R.string.items_deleted_message)
            _eventLogoutRequest.value = true
            viewModelScope.launch {
                repo.clearAllLocal()
            }
        }
    }

    fun onSingOut() {
        _eventLogoutRequest.value = false
        tokenManager.clearAuthToken()
    }

    fun setNetworkStatus(b: Boolean) {
        TODO("Not yet implemented")
    }

    fun onPermissionsCheckFailed() {
        TODO("Not yet implemented")
    }

    fun onPermissionsCheckPassed() {
        TODO("Not yet implemented")
    }
}