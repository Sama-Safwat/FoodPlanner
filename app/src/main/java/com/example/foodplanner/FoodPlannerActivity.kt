package com.example.foodplanner

import android.app.Application
import com.example.foodplanner.data.local.AppDatabase

class FoodPlannerActivity : Application() {

    val database by lazy {
        AppDatabase.getDatabase(this)
    }
}