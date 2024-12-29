package com.arunabha.crudtodoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arunabha.crudtodoapp.data.dao.TodoDao
import com.arunabha.crudtodoapp.data.model.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {

        // database name
        private const val DATABASE_NAME = "todo_db"

        // get instance of the database
        fun getDatabase(context: Context): TodoDatabase {
            return Room.databaseBuilder(
                context,
                TodoDatabase::class.java,
                DATABASE_NAME

                // callback to add dummy data
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initBasicData(context)
                }


                fun initBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val todoDao = getDatabase(context).todoDao()

                        // dummy data populating
                        // these are to get started with, we can delete it using delete item functionality of the app
                        val todo1 = TodoItem(
                            null,
                            "new app",
                            "i will have to complete the new app by next monday!!",
                            "12/12/2024",
                            true
                        )
                        val todo2 = TodoItem(
                            null,
                            "room cleaning",
                            "today i have to clean my room!!",
                            "15/12/2024",
                            false
                        )

                        todoDao.addTodo(todo1)
                        todoDao.addTodo(todo2)
                    }
                }
            }).build()
        }
    }
}