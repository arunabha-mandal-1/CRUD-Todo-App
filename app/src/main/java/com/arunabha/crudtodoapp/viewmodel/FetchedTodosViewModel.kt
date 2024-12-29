package com.arunabha.crudtodoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arunabha.crudtodoapp.api.RetrofitClient
import com.arunabha.crudtodoapp.data.model.FetchedTodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FetchedTodosViewModel : ViewModel() {

    // fetched todoItems
    private val _fetchedTodoList = MutableStateFlow<List<FetchedTodoItem>>(emptyList())
    val fetchedTodoList: StateFlow<List<FetchedTodoItem>> get() = _fetchedTodoList

    private val _isAllLoaded = MutableStateFlow(false)
    val isAllLoaded: StateFlow<Boolean> get() = _isAllLoaded

    // get items by id
    private suspend fun getTodoById(id: Int): FetchedTodoItem? {
        return try {
            RetrofitClient.apiService.getTodoById(id)
        } catch (e: Throwable) {
            null
        }
    }


    fun loadData(){
        // so that it does not block the main thread IO dispatcher is used
        viewModelScope.launch(Dispatchers.IO) {
            val todoList = _fetchedTodoList.value.toMutableList()
            var i = 1
            var flag = true
            while (flag){
                val todo = getTodoById(i)
                if(todo?.id != null){
                    todoList += todo

                    // tasks will be loaded one by one
                    _fetchedTodoList.value += todo
                    i += 1
                }else{
                    flag = false
                }
            }
            _isAllLoaded.value = true
        }
    }


    init {
        loadData()
    }
}