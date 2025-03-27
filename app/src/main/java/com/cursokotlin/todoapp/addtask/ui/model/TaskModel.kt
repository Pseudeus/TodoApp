package com.cursokotlin.todoapp.addtask.ui.model

data class TaskModel(
    val id: Long = System.currentTimeMillis(),
    val task: String,
    var checked: Boolean = false
)