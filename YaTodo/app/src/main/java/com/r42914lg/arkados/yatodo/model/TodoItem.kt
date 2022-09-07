package com.r42914lg.arkados.yatodo.model

import com.r42914lg.arkados.yatodo.database.DbTodoItem
import java.util.*

data class TodoItem(
    var id: Int? = null,
    var text: String  = "",
    var importance: Importance = DEFAULT,
    var done: Boolean = false,
    val created: Date = Calendar.getInstance().time,
    var deadline: String? = null,
    var changed: Date? = null,
    var deletePending: Boolean = false
)

fun TodoItem.asDatabaseModel(): DbTodoItem =
    DbTodoItem(
            id = this.id,
            text = this.text,
            importance = this.importance.intRep(),
            done = this.done,
            created = this.created.time,
            deadline = this.deadline,
            changed = this.changed?.time,
            deletePending = this.deletePending
    )

fun List<TodoItem>.countCompleted() : Int =
    count { it.done }

fun List<TodoItem>.filterNonCompletedOnly() : List<TodoItem> = filter { !it.done }