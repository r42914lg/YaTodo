package com.r42914lg.arkados.yatodo.ui

import androidx.recyclerview.widget.DiffUtil
import com.r42914lg.arkados.yatodo.model.TodoItem

class WorkItemDiffUtils(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].text == newList[newItemPosition].text
                && oldList[oldItemPosition].done == newList[newItemPosition].done
                && oldList[oldItemPosition].deadline == newList[newItemPosition].deadline
                && oldList[oldItemPosition].importance == newList[newItemPosition].importance
}