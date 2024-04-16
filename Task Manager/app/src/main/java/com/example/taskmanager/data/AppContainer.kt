package com.example.taskmanager.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val taskRepository: TaskRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val taskRepository: TaskRepository by lazy {
        TaskRepository(TaskDatabase.getDatabase(context).taskDao())
    }
}
