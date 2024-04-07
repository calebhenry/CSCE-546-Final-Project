package com.example.taskmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.Routine
import com.example.taskmanager.data.RoutinesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    private val routinesRepository: RoutinesRepository
) : ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        routinesRepository.getAllRoutines().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    fun addTempRoutine() {
        viewModelScope.launch {
            routinesRepository.insertRoutine(
                Routine(
                    routineName = "Get jacked with caleb",
                    routineDescription = "caleb isnt jacked :("
                )
            )
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val routineList: List<Routine> = listOf()
)