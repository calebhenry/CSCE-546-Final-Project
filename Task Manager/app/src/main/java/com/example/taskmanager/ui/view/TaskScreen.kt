package com.example.taskmanager.ui.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.data.DateUtils
import com.example.taskmanager.data.Task
import com.example.taskmanager.navigation.AppBar
import com.example.taskmanager.navigation.NavigationDestination
import com.example.taskmanager.ui.AppViewModelProvider
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TaskScreenDestination : NavigationDestination {
    override val route = "task screen"
    override val titleRes = R.string.task_screen_title
    const val taskIdArg = "id" // assuming this isn't what the actual key is in the savedstatehandle
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen (
    navigateBack: () -> Unit,
    viewModel: TaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val currentTask = viewModel.uiState.collectAsState().value
    val currentTaskName = currentTask.taskName
    val currentTaskDescription = currentTask.taskDescription
    val currentTaskCompletion = currentTask.taskCompletion
    val currentTaskDueDate = currentTask.dueDate

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text(currentTaskName) },
                actions = {
                    IconButton(onClick = {
                        viewModel.deleteTask()
                        navigateBack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Description:",
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
                    .background(Color.LightGray, MaterialTheme.shapes.large).padding(8.dp),
                text = currentTaskDescription
            )
            Row(
                modifier = Modifier.padding(bottom = 5.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Due Date: ${DateUtils.convertMillisToString(currentTaskDueDate)}",
                    fontSize = 24.sp
                )
            }
            Row(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.shapes.large
                )
                    .fillMaxWidth()
                    .padding(15.dp)
                    .padding(end = 15.dp)
                    .height(25.dp)
            ) {
                Text(
                    text = "Complete:",
                    modifier = Modifier.fillMaxSize()
                        .align(Alignment.CenterVertically)
                )
                Checkbox(
                    checked = currentTaskCompletion,
                    onCheckedChange = { isChecked -> viewModel.handleCheck(complete = isChecked) },
                )
            }
        }
    }
}