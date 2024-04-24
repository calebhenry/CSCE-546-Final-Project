package com.example.taskmanager.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskmanager.TaskManagerApplication
import com.example.taskmanager.ui.home.HomeViewModel
import com.example.taskmanager.ui.routine.TaskEntryViewModel
import com.example.taskmanager.ui.view.TaskViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(taskApplication().container.taskRepository)
        }
        initializer {
            TaskEntryViewModel(taskApplication().container.taskRepository)
        }
        initializer {
            TaskViewModel(taskApplication().container.taskRepository, this.createSavedStateHandle())
        }
    }
}

fun CreationExtras.taskApplication(): TaskManagerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskManagerApplication)