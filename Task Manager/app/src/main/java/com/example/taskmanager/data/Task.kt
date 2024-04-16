package com.example.taskmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
   @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
   @ColumnInfo(name = "task_name")
    val taskName: String,
   @ColumnInfo(name = "description")
    val taskDescription: String,
    @ColumnInfo(name = "completed")
    val taskCompletion: Boolean,
    @ColumnInfo(name = "due_date")
    val dueDate: Long
)
