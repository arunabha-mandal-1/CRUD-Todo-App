package com.arunabha.crudtodoapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arunabha.crudtodoapp.data.TodoDatabase
import com.arunabha.crudtodoapp.data.dao.TodoDao
import com.arunabha.crudtodoapp.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MyTodosViewModel(private val dao: TodoDao) : ViewModel() {

    // get all todos
    val todos = dao.getAllTodos().map { todoList ->
        todoList.sortedByDescending { it.date }
    }

    // delete one
    suspend fun deleteTodo(todoItem: TodoItem): Boolean {
        return try {
            dao.deleteTodo(todoItem)
            true
        } catch (e: Throwable) {
            false
        }
    }

    // update one
    suspend fun updateTodo(todoItem: TodoItem): Boolean {
        return try {
            dao.updateTodo(todoItem)
            true
        } catch (e: Throwable) {
            false
        }
    }
}

class MyTodosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyTodosViewModel::class.java)) {
            val dao = TodoDatabase.getDatabase(context).todoDao()
            @Suppress("UNCHECKED_CAST")
            return MyTodosViewModel(dao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}