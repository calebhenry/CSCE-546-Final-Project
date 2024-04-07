package com.example.taskmanager.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val routinesRepository: RoutinesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val routinesRepository: RoutinesRepository by lazy {
        RoutinesRepository(RoutineDatabase.getDatabase(context).routineDao())
    }
}
