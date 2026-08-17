package com.example.foodplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_plan")
data class PlannedMealEntity(
    @PrimaryKey(autoGenerate = true) val planId: Int = 0,
    val day: String,          // e.g. "Saturday"
    val mealId: String,
    val mealName: String,
    val mealImageUrl: String
)