package com.example.foodplanner

import android.app.Application
import com.example.foodplanner.data.local.AppDatabase
import com.example.foodplanner.data.repository.FavoritesRepository
import com.example.foodplanner.data.repository.WeeklyPlanRepository
import com.google.firebase.auth.FirebaseAuth
class App : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val favoritesRepository by lazy { FavoritesRepository(database.favoritesDao()) }

    val planRepository by lazy { WeeklyPlanRepository(database.planDao()) }
    val currentUserId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
}