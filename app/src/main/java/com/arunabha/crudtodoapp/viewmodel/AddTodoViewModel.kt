package com.arunabha.crudtodoapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arunabha.crudtodoapp.data.TodoDatabase
import com.arunabha.crudtodoapp.data.dao.TodoDao
import com.arunabha.crudtodoapp.data.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTodoViewModel(private val dao: TodoDao): ViewModel() {

    // task title
    private val _titleText = MutableStateFlow("")
    val titleText: StateFlow<String> get() = _titleText // getter
    fun setTitle(title: String){ // setter
        _titleText.value = title
    }

    // description text
    private val _descText = MutableStateFlow("")
    val descText: StateFlow<String> get() = _descText // getter
    fun setDesc(desc: String){ // setter
        _descText.value = desc
    }



    // add one
    suspend fun addTodo(todoItem: TodoItem): Boolean {
        return try {
            dao.addTodo(todoItem)
            true
        } catch (e: Throwable) {
            false
        }
    }
}

class AddTodoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTodoViewModel::class.java)) {
            val dao = TodoDatabase.getDatabase(context).todoDao()
            @Suppress("UNCHECKED_CAST")
            return AddTodoViewModel(dao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}