package com.example.taskmanager.ui.routine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository

class TaskEntryViewModel(
    private val taskRepository: TaskRepository
): ViewModel() {

    var taskEntryUiState by mutableStateOf(TaskEntryUiState())
        private set

    fun updateUiState(taskName: String, taskDescription: String, dueDate: Long) {
        taskEntryUiState = TaskEntryUiState(taskName, taskDescription, dueDate)
    }

    suspend fun saveRoutine() {
        taskRepository.insertTask(
            Task(
                taskName = taskEntryUiState.taskName,
                taskDescription =  taskEntryUiState.taskDescription,
                taskCompletion = false,
                dueDate = taskEntryUiState.dueDate
            )
        )
    }
}

data class TaskEntryUiState(
    val taskName: String = "",
    val taskDescription: String = "",
    val dueDate: Long = 0
)