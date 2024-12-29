package com.arunabha.crudtodoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.arunabha.crudtodoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    // get all todos
    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): Flow<List<TodoItem>>

    // add one
    @Insert
    suspend fun addTodo(todoItem: TodoItem)

    // delete one
    @Delete
    suspend fun deleteTodo(todoItem: TodoItem)

    // update one
    @Update
    suspend fun updateTodo(todoItem: TodoItem)

}