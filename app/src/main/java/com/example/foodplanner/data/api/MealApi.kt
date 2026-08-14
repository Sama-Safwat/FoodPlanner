package com.example.foodplanner.data.api

import com.example.foodplanner.data.model.CategoryResponse
import com.example.foodplanner.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    // Search meal by name
    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") mealName: String
    ): MealResponse


    // Search meals by first letter
    @GET("search.php")
    suspend fun searchMealsByFirstLetter(
        @Query("f") letter: String
    ): MealResponse


    // Get full meal details by ID
    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i") mealId: String
    ): MealResponse


    // Get a single random meal
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse


    // Get all meal categories
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse


    // Get meals by category
    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealResponse


    // Get meals by country / area
    @GET("filter.php")
    suspend fun getMealsByArea(
        @Query("a") area: String
    ): MealResponse


    // Get meals by main ingredient
    @GET("filter.php")
    suspend fun getMealsByIngredient(
        @Query("i") ingredient: String
    ): MealResponse
}