package com.arunabha.crudtodoapp.api

import com.arunabha.crudtodoapp.data.model.FetchedTodoItem
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {

    // get one fetchedTodoItem at a time
    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): FetchedTodoItem
}