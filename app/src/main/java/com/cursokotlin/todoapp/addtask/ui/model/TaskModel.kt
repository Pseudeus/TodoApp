package com.cursokotlin.todoapp.addtask.ui.model

data class TaskModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    val task: String,
    var checked: Boolean = false
)