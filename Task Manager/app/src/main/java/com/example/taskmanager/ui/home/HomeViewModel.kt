package com.example.taskmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        taskRepository.getAllTasks().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    fun handleCheck(complete: Boolean, task: Task) {
        viewModelScope.launch {
            if(complete) {
                completeTask(task)
            } else {
                uncompleteTask(task)
            }
        }
    }
    private suspend fun uncompleteTask(task: Task) {
        taskRepository.updateTask(
            Task(
                id = task.id,
                taskName = task.taskName,
                taskDescription = task.taskDescription,
                taskCompletion = false,
                dueDate = task.dueDate
            )
        )
    }
    private suspend fun completeTask(task: Task) {
        taskRepository.updateTask(
            Task(
                id = task.id,
                taskName = task.taskName,
                taskDescription = task.taskDescription,
                taskCompletion = true,
                dueDate = task.dueDate
            )
        )
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val taskList: List<Task> = listOf()
)