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

class DetailsVm @AssistedInject constructor(
    private val app: Application,
    private val repo: IRepo,
) : AndroidViewModel(app) {

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    @AssistedFactory
    interface Factory {
        fun create(): DetailsVm
    }

    fun newItem() {
        _todoItem.value = TodoItem()
    }

    fun onOpenItem(todoItem: TodoItem) {
        _todoItem.value = todoItem
    }

    fun onSaveItem(text: String,
                   importance: Importance,
                   deadline: String) {

        _todoItem.value?.let {
            it.text = text
            it.deadline = deadline
            it.importance = importance
            it.changed = Calendar.getInstance().time

            viewModelScope.launch {
                if (it.id == null)
                    repo.addTodo(it)
                else
                    repo.updateTodo(it)
            }
        }
    }

    fun onDelete() {
        _todoItem.value?.let {
            it.changed = Calendar.getInstance().time
            it.deletePending = true
            viewModelScope.launch {
                repo.updateTodo(it)
            }
        }
    }
}