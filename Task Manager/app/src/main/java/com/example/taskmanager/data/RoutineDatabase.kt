package com.example.taskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Routine::class], version = 1, exportSchema = false)
abstract class RoutineDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var Instance: RoutineDatabase? = null

        fun getDatabase(context: Context): RoutineDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RoutineDatabase::class.java, "routine_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}