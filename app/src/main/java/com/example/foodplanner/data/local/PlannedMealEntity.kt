package com.example.foodplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
@Entity(tableName = "weekly_plan",
    indices = [Index(value = ["date", "mealId"], unique = true)])
data class PlannedMealEntity(
    @PrimaryKey(autoGenerate = true) val planId: Int = 0,
    val date: String,
    val mealId: String,
    val mealName: String,
    val mealImageUrl: String
)