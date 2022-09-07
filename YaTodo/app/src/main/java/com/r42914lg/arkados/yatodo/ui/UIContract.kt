package com.r42914lg.arkados.yatodo.ui

import com.r42914lg.arkados.yatodo.model.TodoItem

interface IMainView {}

interface ITodoListView {
    fun setTitle(text: String)
    fun setSubtitle(text: String)
    fun setItems(newItems: List<TodoItem>)
}

interface ITodoDetailsView {
    fun setImportance(text: String)
    fun setDeadline(text: String)
    fun setTodoText(text: String)
    fun setDateSwitchChecked(boolean: Boolean)
}
