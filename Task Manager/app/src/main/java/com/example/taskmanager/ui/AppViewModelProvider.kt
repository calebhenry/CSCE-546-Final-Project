package com.example.taskmanager.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskmanager.TaskManagerApplication
import com.example.taskmanager.ui.home.HomeViewModel
import com.example.taskmanager.ui.routine.RoutineEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(gymApplication().container.routinesRepository)
        }
        initializer {
            RoutineEntryViewModel(gymApplication().container.routinesRepository)
        }
    }
}

fun CreationExtras.gymApplication(): TaskManagerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskManagerApplication)