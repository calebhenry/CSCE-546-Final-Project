package com.example.taskmanager

import android.app.Application
import com.example.taskmanager.data.AppContainer
import com.example.taskmanager.data.AppDataContainer

class TaskManagerApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}