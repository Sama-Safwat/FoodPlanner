package com.example.foodplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals",
primaryKeys = ["userId", "idMeal"])
data class MealEntity @JvmOverloads constructor(
    val userId: String = "",
    val idMeal: String = "",
    val strMeal: String = "",
    val strMealThumb: String? = null,
    val strCategory: String? = null,
    val strArea: String? = null,
    val strInstructions: String? = null,
    val strYoutube: String? = null
)