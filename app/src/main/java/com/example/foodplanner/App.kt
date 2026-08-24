package com.example.foodplanner

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import com.example.foodplanner.data.local.AppDatabase
import com.example.foodplanner.data.repository.FavoritesRepository
import com.example.foodplanner.data.repository.WeeklyPlanRepository
import com.example.foodplanner.data.sync.SyncManager

class App : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val syncManager by lazy { SyncManager(database.mealDao(), database.planDao()) }
    val favoritesRepository by lazy { FavoritesRepository(database.favoritesDao(), syncManager) }
    val planRepository by lazy { WeeklyPlanRepository(database.planDao(), syncManager) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}