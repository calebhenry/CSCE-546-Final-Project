package com.example.taskmanager.ui.routine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.Routine
import com.example.taskmanager.data.RoutinesRepository

class RoutineEntryViewModel(
    private val routinesRepository: RoutinesRepository
): ViewModel() {

    var routineEntryUiState by mutableStateOf(RoutineEntryUiState())
        private set

    fun updateUiState(routineName: String, routineDescription: String) {
        routineEntryUiState = RoutineEntryUiState(routineName, routineDescription)
    }

    suspend fun saveRoutine() {
        routinesRepository.insertRoutine(
            Routine(
                routineName = routineEntryUiState.routineName,
                routineDescription =  routineEntryUiState.routineDescription
            )
        )
    }
}

data class RoutineEntryUiState(
    val routineName: String = "",
    val routineDescription: String = ""
)