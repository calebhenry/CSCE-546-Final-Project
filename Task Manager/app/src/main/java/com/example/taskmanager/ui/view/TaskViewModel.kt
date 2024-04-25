package com.example.taskmanager.ui.view

import android.os.Debug
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Task

import com.example.taskmanager.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * This view model satisfies the data requirement
 */
class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: Long = checkNotNull(savedStateHandle[TaskScreenDestination.taskIdArg]) // this is coming back null

    val uiState: StateFlow<CurrentTaskUiState> =
        taskRepository.getTask(id)
            .filterNotNull()
            .map {
                CurrentTaskUiState(
                    taskName = it.taskName,
                    taskDescription = it.taskDescription,
                    taskCompletion = it.taskCompletion,
                    dueDate = it.dueDate
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = CurrentTaskUiState()
            )

    fun handleCheck(complete: Boolean) {
        viewModelScope.launch {
            updateTaskCompletion(complete)
        }
    }
    suspend fun updateTaskCompletion(status: Boolean) {
        val currentTask = uiState.value
        taskRepository.updateTask(
            Task(
                id = id,
                taskName = currentTask.taskName,
                taskDescription = currentTask.taskDescription,
                taskCompletion = status,
                dueDate = currentTask.dueDate
            )
        )
    }

    fun deleteTask() {
        viewModelScope.launch {
            taskRepository.deleteTask(id);
        }
    }
}

data class CurrentTaskUiState (
    val taskName: String = "",
    val taskDescription: String = "",
    var taskCompletion: Boolean = false,
    val dueDate: Long = 0
)

