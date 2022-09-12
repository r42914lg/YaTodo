package com.r42914lg.arkados.yatodo.database

import androidx.room.*

@Dao
interface TodoDao {
    @Insert
    fun insertItem(item: DbTodoItem)

    @Insert
    fun insertAll(items: List<DbTodoItem>)

    @Update
    fun updateItem(item: DbTodoItem)

    @Delete
    fun deleteItem(item: DbTodoItem)

    @Query("SELECT * FROM dbtodoitem")
    fun getAllItems() : List<DbTodoItem>

    @Query("SELECT * FROM dbtodoitem WHERE deletepending == 0")
    fun getNoDelItems() : List<DbTodoItem>

    @Query("SELECT * FROM dbtodoitem WHERE id == :localid")
    fun isRecordExist(localid: Int): Boolean

    @Query("DELETE FROM dbtodoitem")
    fun deleteAll()
}

@Database(entities = [DbTodoItem::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract val todoDao: TodoDao
}