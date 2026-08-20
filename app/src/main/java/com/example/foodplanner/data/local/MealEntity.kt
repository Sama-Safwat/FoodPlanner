package com.example.foodplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals",
primaryKeys = ["userId", "idMeal"])
data class MealEntity(

    val idMeal: String,
    val userId: String,
    val strMeal: String?,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    val strYoutube: String?
)