package com.example.taskmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine)

    @Query("SELECT * from routines where id = :id")
    fun getRoutine(id: Int): Flow<Routine>

    @Query("SELECT * from routines ORDER BY routine_name ASC")
    fun getAllItems(): Flow<List<Routine>>
}