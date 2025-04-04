package com.cursokotlin.todoapp.addtask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.todoapp.addtask.domain.AddTaskUseCase
import com.cursokotlin.todoapp.addtask.domain.DeleteTaskUseCase
import com.cursokotlin.todoapp.addtask.domain.GetTasksUseCase
import com.cursokotlin.todoapp.addtask.domain.UpdateTaskUseCase
import com.cursokotlin.todoapp.addtask.ui.TaskUiState.Error
import com.cursokotlin.todoapp.addtask.ui.TaskUiState.Success
import com.cursokotlin.todoapp.addtask.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TaskUiState.Loading)


    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onShowDialogClose() {
        _showDialog.postValue(false)
    }
    fun onShowDialogClick() {
        _showDialog.postValue(true)
    }

    fun onTaskCreated(task: String) {
        _showDialog.postValue(false)

        viewModelScope.launch(Dispatchers.IO) {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onCheckBoxSelected(model: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase(model.copy(checked = !model.checked))
        }
    }

    fun onItemRemoved(model: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTaskUseCase(model)
        }
    }
}