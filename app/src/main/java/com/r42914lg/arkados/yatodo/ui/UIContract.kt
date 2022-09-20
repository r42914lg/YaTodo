package com.r42914lg.arkados.yatodo.ui

import com.r42914lg.arkados.yatodo.model.Importance
import com.r42914lg.arkados.yatodo.model.TodoItem

interface IMainView {
    fun setUserDetails(hideFlag: Boolean, text: String?)
    fun setNetworkStatus(text: String)
    fun toast(text: String)
    fun snackBarWithText(text: String)
    fun showFab(showFlag: Boolean)
    fun animateFab(animateFlag: Boolean)
}

interface ITodoListView {
    fun setTitle(text: String)
    fun setSubtitle(text: String)
    fun setItems(newItems: MutableList<TodoItem>)
    fun toast(text: String)
}

interface ITodoDetailsView {
    fun setImportance(importance: Importance)
    fun setDeadline(text: String)
    fun setTodoText(text: String)
    fun setDateSwitchChecked(boolean: Boolean)
}
