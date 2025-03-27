package com.cursokotlin.todoapp.addtask.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: Int = 0,
    val task: String,
    var checked: Boolean = false
)