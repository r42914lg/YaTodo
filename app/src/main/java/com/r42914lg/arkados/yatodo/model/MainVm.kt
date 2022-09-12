package com.r42914lg.arkados.yatodo.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.r42914lg.arkados.yatodo.repository.IRepo
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.*

class MainVm @AssistedInject constructor(
    private val app: Application,
    private val repo: IRepo,
) : AndroidViewModel(app) {

    @AssistedFactory
    interface Factory {
        fun create(): MainVm
    }

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
        viewModelScope.launch {
            item.deletePending = true
            repo.updateTodo(item)
        }
    }

    fun onCompleteChanged(item: TodoItem) {
        viewModelScope.launch {
            item.changed = Calendar.getInstance().time
            repo.updateTodo(item)
        }
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