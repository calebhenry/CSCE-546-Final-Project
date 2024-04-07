package com.example.taskmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
   @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
   @ColumnInfo(name = "routine_name")
    val routineName: String,
   @ColumnInfo(name = "description")
    val routineDescription: String
)
