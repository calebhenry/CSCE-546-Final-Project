package com.example.taskmanager.ui.routine

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.data.Task
import com.example.taskmanager.navigation.AppBar
import com.example.taskmanager.navigation.NavigationDestination
import com.example.taskmanager.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.taskmanager.data.DateUtils

object TaskEntryDestination : NavigationDestination {
    override val route = "task entry"
    override val titleRes = R.string.task_entry_title
}

/**
 * This screen contains text input, buttons, and date input, satisfying the user interaction
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.taskEntryUiState
    val task = Task(
        taskName = uiState.taskName,
        taskDescription = uiState.taskDescription,
        taskCompletion = false,
        dueDate = uiState.dueDate
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            AppBar(
                title = stringResource(id = R.string.task_entry_title),
                canNavigateBack = true,
                navigateUp = { navigateUp() }
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            task = task,
            onValueChange = viewModel::updateUiState,
            onSaveButtonClick = {
                coroutineScope.launch {
                    viewModel.saveRoutine()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }



}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryBody(
    task: Task,
    onValueChange: (String, String, Long) -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDatePickerOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column (
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = task.taskName,
            onValueChange = {onValueChange(it, task.taskDescription, task.dueDate)},
            label = { Text("Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = task.taskDescription,
            onValueChange = {onValueChange(task.taskName, it, task.dueDate)},
            label = { Text("Description") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if(task.dueDate == 0L) {
            Button(
                onClick = { isDatePickerOpen = true },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Select Due Date")
            }
        } else {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = DateUtils.convertMillisToString(task.dueDate))
                Button(
                    onClick = { isDatePickerOpen = true },
                    contentPadding = PaddingValues(0.dp),
                    shape = CircleShape,
                    modifier = Modifier.width(40.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
            }

        }
        if(isDatePickerOpen) {
            DatePickerDialog(
                onDismissRequest = { isDatePickerOpen = false },
                confirmButton = {
                    Button(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onValueChange(task.taskName, task.taskDescription,
                                it
                            )
                        }
                        isDatePickerOpen = false
                    }
                    ) {
                        Text(text = "Close")
                    }

                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Button(
            onClick = onSaveButtonClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = "Save")
        }
    }
}