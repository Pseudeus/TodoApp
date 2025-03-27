package com.cursokotlin.todoapp.addtask.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cursokotlin.todoapp.addtask.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor() : ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: List<TaskModel> = _tasks

    fun onShowDialogClose() {
        _showDialog.postValue(false)
    }
    fun onShowDialogClick() {
        _showDialog.postValue(true)
    }

    fun onTaskCreated(task: String) {
        _showDialog.postValue(false)
        _tasks.add(TaskModel(task = task))
    }

    fun onCheckBoxSelected(model: TaskModel) {

        val index = _tasks.indexOf(model)
        _tasks[index] = _tasks[index].let {
            it.copy(checked = !it.checked)
        }
    }

    fun onItemRemoved(model: TaskModel) {
        val task = _tasks.find { it.id == model.id }
        _tasks.remove(task)
    }
}