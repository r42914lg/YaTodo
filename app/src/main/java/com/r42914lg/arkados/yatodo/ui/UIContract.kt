package com.r42914lg.arkados.yatodo.ui

import com.r42914lg.arkados.yatodo.model.TodoItem

interface IMainView {
    fun setUserDetails(hideFlag: Boolean, text: String?)
    fun setNetworkStatus(text: String)
    fun toast(text: String)
    fun snackBarWithText(text: String)
}

interface ITodoListView {
    fun setTitle(text: String)
    fun setSubtitle(text: String)
    fun setItems(newItems: MutableList<TodoItem>)
    fun toast(text: String)
}

interface ITodoDetailsView {
    fun setImportance(text: String)
    fun setDeadline(text: String)
    fun setTodoText(text: String)
    fun setDateSwitchChecked(boolean: Boolean)
}
