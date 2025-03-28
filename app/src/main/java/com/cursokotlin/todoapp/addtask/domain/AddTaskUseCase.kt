package com.cursokotlin.todoapp.addtask.domain

import com.cursokotlin.todoapp.addtask.data.TaskRepository
import com.cursokotlin.todoapp.addtask.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.add(taskModel)
    }
}