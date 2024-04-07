package com.example.taskmanager.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanager.R
import com.example.taskmanager.data.Routine
import com.example.taskmanager.navigation.AppBar
import com.example.taskmanager.navigation.NavigationDestination
import com.example.taskmanager.ui.AppViewModelProvider
import kotlinx.coroutines.launch

object RoutineEntryDestination : NavigationDestination {
    override val route = "routine entry"
    override val titleRes = R.string.routine_entry_title
}

@Composable
fun RoutineEntryScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: RoutineEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.routineEntryUiState
    val routine = Routine(
        routineName = uiState.routineName, 
        routineDescription = uiState.routineDescription
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            AppBar(
                title = stringResource(id = R.string.routine_entry_title),
                canNavigateBack = true,
                navigateUp = { navigateUp() }
            )
        }
    ) { innerPadding ->
        RoutineEntryBody(
            routine = routine,
            onValueChange = viewModel::updateUiState,
            onSaveButtonClick = {
                coroutineScope.launch {
                    viewModel.saveRoutine()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding).fillMaxWidth()
        )
    }



}

@Composable
fun RoutineEntryBody(
    routine: Routine,
    onValueChange: (String, String) -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = routine.routineName,
            onValueChange = {onValueChange(it, routine.routineDescription)},
            label = { Text("Name") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = routine.routineDescription,
            onValueChange = {onValueChange(routine.routineName, it)},
            label = { Text("Description") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveButtonClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = "Save")
        }
    }
}