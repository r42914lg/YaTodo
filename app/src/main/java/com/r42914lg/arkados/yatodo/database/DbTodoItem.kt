package com.r42914lg.arkados.yatodo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.r42914lg.arkados.yatodo.model.Importance
import com.r42914lg.arkados.yatodo.model.TodoItem
import com.r42914lg.arkados.yatodo.network.NetworkTotoItem
import java.util.*

@Entity
class DbTodoItem constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    var text: String,
    val importance: Int,
    val done: Boolean,
    val created: Long,
    val deadline: String?,
    val changed: Long?,
    val deletepending: Boolean
)

fun List<DbTodoItem>.asDomainModel(): MutableList<TodoItem> {
    return map {
        TodoItem(
            id = it.id,
            text = it.text,
            importance = Importance.Factory().parseFromInt(it.importance),
            done = it.done,
            created = Date(it.created),
            deadline = it.deadline,
            changed = it.changed?.let { it1 -> Date(it1) },
            deletepending = it.deletepending)
    }.toMutableList()
}

fun List<DbTodoItem>.asNetworkModel(): List<NetworkTotoItem> {
    return map {
        NetworkTotoItem(
            localid = it.id!!,
            text = it.text,
            importance = it.importance,
            done = it.done,
            created = it.created,
            deadline = it.deadline,
            changed = it.changed!!,
            deletepending = it.deletepending)
    }
}