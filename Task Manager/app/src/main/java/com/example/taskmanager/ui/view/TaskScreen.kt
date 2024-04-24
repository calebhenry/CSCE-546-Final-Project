package com.example.taskmanager.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.navigation.AppBar
import com.example.taskmanager.navigation.NavigationDestination
import com.example.taskmanager.ui.AppViewModelProvider

object TaskScreenDestination : NavigationDestination {
    override val route = "task screen"
    override val titleRes = R.string.task_screen_title
    const val taskIdArg = "id" // assuming this isn't what the actual key is in the savedstatehandle
}

@Composable
fun TaskScreen (
    navigateBack: () -> Unit,
    viewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val currentTask = viewModel.currentTaskUiState
    val currentTaskName = currentTask.value.taskName
    val currentTaskDescription = currentTask.value.taskDescription
    val currentTaskCompletion = currentTask.value.taskCompletion
    val currentTaskDueDate = currentTask.value.dueDate

    Scaffold(
        topBar = {
            AppBar(
                title = currentTaskName,
                canNavigateBack = true,
                navigateUp = {}
            )
        }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}