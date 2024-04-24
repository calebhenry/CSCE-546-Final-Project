package com.example.taskmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Query("SELECT * from tasks where id = :id")
    fun getTask(id: Long): Flow<Task>

    @Query("SELECT * from tasks ORDER BY task_name ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT task_name from tasks where id = :id")
    fun getTaskName(id: Long): String

    @Query("SELECT description from tasks where id = :id")
    fun getTaskDescription(id: Long): String

    @Query("SELECT completed from tasks where id = :id")
    fun getTaskCompletion(id: Long): Boolean

    @Query("SELECT due_date from tasks where id = :id")
    fun getTaskDueDate(id: Long): Long
}
