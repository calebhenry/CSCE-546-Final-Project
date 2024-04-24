package com.example.taskmanager.ui.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.Task

import com.example.taskmanager.data.TaskRepository

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: Long = checkNotNull(savedStateHandle[TaskScreenDestination.taskIdArg]) // this is coming back null

    var currentTaskUiState = mutableStateOf(
        CurrentTaskUiState(
            taskName = taskRepository.getTaskName(id),
            taskDescription = taskRepository.getTaskDescription(id),
            taskCompletion = taskRepository.getTaskCompletion(id),
            dueDate = taskRepository.getTaskDueDate(id)
        )
    )
        private set

    fun updateTaskCompletion() {
        currentTaskUiState.value.taskCompletion = !currentTaskUiState.value.taskCompletion
    }

    suspend fun saveRoutine() {
        taskRepository.updateTask(
            Task(
                taskName = currentTaskUiState.value.taskName,
                taskDescription = currentTaskUiState.value.taskDescription,
                taskCompletion = currentTaskUiState.value.taskCompletion,
                dueDate = currentTaskUiState.value.dueDate
            )
        )
    }
}

data class CurrentTaskUiState (
    val taskName: String = "",
    val taskDescription: String = "",
    var taskCompletion: Boolean = false,
    val dueDate: Long = 0
)

