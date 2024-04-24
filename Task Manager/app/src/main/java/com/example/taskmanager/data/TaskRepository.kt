package com.example.taskmanager.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTask(id: Long): Flow<Task?> = taskDao.getTask(id)

    fun getTaskName(id: Long): String = taskDao.getTaskName(id)

    fun getTaskDescription(id: Long): String = taskDao.getTaskDescription(id)

    fun getTaskCompletion(id: Long): Boolean = taskDao.getTaskCompletion(id)

    fun getTaskDueDate(id: Long): Long = taskDao.getTaskDueDate(id)

    suspend fun insertTask(task: Task) = taskDao.insert(task)

    suspend fun updateTask(task: Task) = taskDao.update(task)
}