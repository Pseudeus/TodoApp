package com.cursokotlin.todoapp.addtask.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.cursokotlin.todoapp.addtask.ui.model.TaskModel

@Composable
fun TasksScreen(viewModel: TasksViewModel, modifier: Modifier) {
    val showDialog: Boolean by viewModel.showDialog.observeAsState(initial = false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<TaskUiState>(
        TaskUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }

    when (uiState) {
        is TaskUiState.Error -> {}
        TaskUiState.Loading -> {
            CircularProgressIndicator()
        }
        is TaskUiState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                FabDialog(viewModel, Modifier.align(Alignment.BottomEnd))
                AddTasksDialog(
                    showDialog,
                    onDismiss = { viewModel.onShowDialogClose() }
                ) { viewModel.onTaskCreated(it) }

                TasksList((uiState as TaskUiState.Success).tasks, viewModel)
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<TaskModel>, viewModel: TasksViewModel) {
    LazyColumn {
        items(tasks, key = { it.id }) {
            ItemTask(it, viewModel)
        }
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, viewModel: TasksViewModel) {
    Card(
        Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    viewModel.onItemRemoved(taskModel)
                })
            },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(2.dp, Color.Magenta)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(taskModel.task, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Checkbox(
                checked = taskModel.checked,
                onCheckedChange = { viewModel.onCheckBoxSelected(taskModel) })
        }
    }
}

@Composable
fun FabDialog(viewModel: TasksViewModel, modifier: Modifier) {
    FloatingActionButton(onClick = { viewModel.onShowDialogClick() }, modifier.padding(24.dp)) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "addTodo")
    }
}

@Composable
fun AddTasksDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember { mutableStateOf("") }

    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        "Añade tu tarea",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(16.dp))
                    TextField(value = myTask, onValueChange = { myTask = it }, singleLine = true)
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            onTaskAdded(myTask)
                            myTask = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Añadir tarea", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}