package com.cursokotlin.todoapp.addtask.data

import com.cursokotlin.todoapp.addtask.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val tasks: Flow<List<TaskModel>> = taskDao.getAllTasks().map { items ->
        items.map {
            TaskModel(it.id, it.task, it.checked)
        }
    }

    suspend fun add(taskItem: TaskModel) {
        taskDao.addTask(TaskEntity(taskItem.id, taskItem.task, taskItem.checked))
    }

    suspend fun update(model: TaskModel) {
        taskDao.updateTask(model.toData())
    }

    suspend fun delete(model: TaskModel) {
        taskDao.deleteTask(model.toData())
    }
}

fun TaskModel.toData(): TaskEntity {
    return TaskEntity(this.id, this.task, this.checked)
}