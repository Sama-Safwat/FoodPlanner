package com.example.foodplanner.data.api

import com.example.foodplanner.data.model.CategoryResponse
import com.example.foodplanner.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Search meal by name
    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") mealName: String
    ): MealResponse

    // Search meals by first letter
    @GET("search.php")
    suspend fun getMealsByFirstLetter(
        @Query("f") letter: String
    ): MealResponse

    // Get meal details by ID
    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i") mealId: String
    ): MealResponse

    // Get a random meal
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    // Get all categories
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    // Get meals by category
    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealResponse

    // Get meals by ingredient
    @GET("filter.php")
    suspend fun getMealsByIngredient(
        @Query("i") ingredient: String
    ): MealResponse

    // Get meals by area
    @GET("filter.php")
    suspend fun getMealsByArea(
        @Query("a") area: String
    ): MealResponse
}