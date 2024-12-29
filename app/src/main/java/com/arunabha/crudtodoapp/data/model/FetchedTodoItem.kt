package com.arunabha.crudtodoapp.data.model

// data class created for todos fetched from the api
data class FetchedTodoItem(
    val userId: Int? = null,
    val id: Int? = null,
    val title: String? = null,
    var completed: Boolean? = null
)
