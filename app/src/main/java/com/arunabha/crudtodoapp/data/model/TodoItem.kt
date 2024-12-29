package com.arunabha.crudtodoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


// data class created for todos stored in local database
@Serializable
@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val desc: String,
    val date: String?,
    var isCompleted: Boolean
)
