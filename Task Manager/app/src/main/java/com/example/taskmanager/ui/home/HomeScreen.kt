package com.example.taskmanager.ui.home

import android.app.Activity
import androidx.annotation.DimenRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@Composable
fun HomeScreen(
    navigateToRoutineEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.homeUiState.collectAsState()
    val tasks = uiState.value.taskList
    val activity = LocalContext.current as Activity

    var showCompletedTasks by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            AppBar(
                title = stringResource(id = R.string.home_title),
                canNavigateBack = false,
                navigateUp = { activity.finish() }
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
            // Incomplete tasks
            LazyColumn (
                modifier = Modifier.padding(innerPadding)
            ) {
                items(tasks.filter { !it.taskCompletion }) { task ->
                    RoutineCard(
                        task = task,
                        onRoutineClick = {},
                        completeTask = { isComplete -> viewModel.handleCheck(complete = isComplete, task = task) }
                    )
                }
            }
            Divider(
                color = Color.LightGray,
                modifier = Modifier.padding(0.dp, 20.dp)
            )
            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Completed",
                    modifier = Modifier.padding(20.dp, 0.dp)
                )
                Spacer(Modifier.weight(1f))
                ItemButton(
                    expanded = showCompletedTasks, 
                    onClick = { showCompletedTasks = !showCompletedTasks }
                )
            }
            if(showCompletedTasks) {
                LazyColumn {
                    items(tasks.filter { it.taskCompletion }) { task ->
                        RoutineCard(
                            task = task,
                            onRoutineClick = {},
                            completeTask = { isComplete -> viewModel.handleCheck(complete = isComplete, task = task) }
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineCard(
    task: Task,
    onRoutineClick: (Task) -> Unit,
    completeTask: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = { onRoutineClick(task) },
        modifier = Modifier.padding(8.dp)
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
        ) {
            Checkbox(
                checked = task.taskCompletion,
                onCheckedChange = completeTask,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Column (
                modifier = Modifier
                    .height(100.dp)
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