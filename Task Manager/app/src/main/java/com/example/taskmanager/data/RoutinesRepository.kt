package com.example.taskmanager.data

import kotlinx.coroutines.flow.Flow

class RoutinesRepository(private val routineDao: RoutineDao) {
    fun getAllRoutines(): Flow<List<Routine>> = routineDao.getAllItems()

    fun getRoutine(id: Int): Flow<Routine?> = routineDao.getRoutine(id)

    suspend fun insertRoutine(routine: Routine) = routineDao.insert(routine)
}