package com.cursokotlin.todoapp.addtask.data.di

import android.content.Context
import androidx.room.Room
import com.cursokotlin.todoapp.addtask.data.TaskDao
import com.cursokotlin.todoapp.addtask.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideTaskDao(todoDatabase: TodoDatabase): TaskDao {
        return todoDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(context, TodoDatabase::class.java, "TaskDatabase").build()
    }
}