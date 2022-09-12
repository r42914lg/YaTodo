package com.r42914lg.arkados.yatodo.network

import com.r42914lg.arkados.yatodo.database.DbTodoItem
import com.r42914lg.arkados.yatodo.model.Importance
import com.r42914lg.arkados.yatodo.model.TodoItem
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class NetworkTotoItem(
    val localid: Int,
    val text: String,
    val importance: Int,
    val done: Boolean,
    val created: Long,
    val deadline: String?,
    val changed: Long,
    val deletepending: Boolean)

fun List<NetworkTotoItem>.asDatabaseModel(): List<DbTodoItem> {
    return this.map {
        DbTodoItem(
            id = it.localid,
            text = it.text,
            importance = it.importance,
            done = it.done,
            created = it.created,
            deadline = it.deadline,
            changed = it.changed,
            deletepending = it.deletepending
        )
    }
}