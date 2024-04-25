package com.example.taskmanager.ui.home

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.data.Task
import com.example.taskmanager.navigation.AppBar
import com.example.taskmanager.navigation.NavigationDestination
import com.example.taskmanager.ui.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.taskmanager.data.DateUtils
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navigateToRoutineEntry: () -> Unit,
    navigateToTaskScreen: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.homeUiState.collectAsState()
    val tasks = uiState.value.taskList
    val groupedCompleteTasks = tasks.filter { it.taskCompletion }.sortedBy { it.dueDate }.groupBy { DateUtils.convertMillisToString(it.dueDate) }
    val groupedIncompleteTasks = tasks.filter { !it.taskCompletion }.sortedBy { it.dueDate }.groupBy { DateUtils.convertMillisToString(it.dueDate) }

    var showCompletedTasks by remember { mutableStateOf(true) }

    Scaffold (
        topBar = {
            val context = LocalContext.current
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text(stringResource(id = R.string.home_title)) },
                actions = {
                    IconButton(onClick = { viewModel.shareTasks(context) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToRoutineEntry() },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "TODO"
                )
            }
        }
    ) { innerPadding ->
        Column {
            LazyColumn (
                modifier = Modifier.padding(innerPadding),
            ) {
                if(groupedIncompleteTasks.isEmpty()) {
                    item {
                        Box (
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nothing left to do right now, add a task to begin!",
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    }
                } else {
                    groupedIncompleteTasks.forEach { (date, tasks) ->
                        item {
                            Box (
                                Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Box(
                                    modifier = Modifier.background(Color.LightGray, MaterialTheme.shapes.large)
                                ) {
                                    Text(
                                        text = date,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                        items(tasks) { task ->
                            RoutineCard(
                                task = task,
                                onRoutineClick = { navigateToTaskScreen(task.id) },
                                completeTask = { isComplete -> viewModel.handleCheck(complete = isComplete, task = task) }
                            )
                        }
                    }
                }
                if(groupedCompleteTasks.isNotEmpty()) {
                    item {
                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier.padding(0.dp, 20.dp)

                        )
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Completed",
                                modifier = Modifier.padding(8.dp, 0.dp)
                            )
                            ItemButton(
                                expanded = showCompletedTasks,
                                onClick = { showCompletedTasks = !showCompletedTasks }
                            )
                        }
                    }
                }
                if(showCompletedTasks) {
                    groupedCompleteTasks.forEach { (date, tasks) ->
                        item {
                            Box (
                                Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Box(
                                    modifier = Modifier.background(Color.LightGray, MaterialTheme.shapes.large)
                                ) {
                                    Text(
                                        text = date,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                        items(tasks) { task ->
                            RoutineCard(
                                task = task,
                                onRoutineClick = { navigateToTaskScreen(task.id) },
                                completeTask = { isComplete -> viewModel.handleCheck(complete = isComplete, task = task) }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(25.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineCard(
    task: Task,
    onRoutineClick: (Task) -> Unit,
    completeTask: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = { onRoutineClick(task) },
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Checkbox(
                checked = task.taskCompletion,
                onCheckedChange = completeTask,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier
                    //.height(150.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = task.taskName,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = task.taskDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    )
                )
            }
        }

    }
}