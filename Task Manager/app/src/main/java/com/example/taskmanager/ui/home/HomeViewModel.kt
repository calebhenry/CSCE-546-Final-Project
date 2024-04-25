package com.example.taskmanager.ui.home

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.DateUtils
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * This view model satisfies the data requirement
 */
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


    /**
     * This sharing function invokes a hardware intent, satisfying the hardware interaction requirement
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun shareTasks(context: Context) {
        val numTasks = getNumTasksForDay()
        val numTasksCompeleted = getNumCompletedTasksForDay()
        val message = "I had $numTasks tasks to complete today, and I completed " +
                if(numTasks == numTasksCompeleted) "all of them!" else "$numTasksCompeleted of them."

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                "Completion Report"
            )
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNumTasksForDay(): Int {
        val date = SimpleDateFormat("MM-dd-yyy").format(Date())
        val list = homeUiState.value.taskList.filter {
            DateUtils.convertMillisToString(it.dueDate) == date
        }
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNumCompletedTasksForDay(): Int {
        val date = SimpleDateFormat("MM-dd-yyy").format(Date())
        val list = homeUiState.value.taskList.filter {
            DateUtils.convertMillisToString(it.dueDate) == date && it.taskCompletion
        }
        return list.size
    }

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