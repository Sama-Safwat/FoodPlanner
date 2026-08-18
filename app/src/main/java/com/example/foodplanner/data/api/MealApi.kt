package com.example.foodplanner.data.api

import com.example.foodplanner.data.model.AreaResponse
import com.example.foodplanner.data.model.CategoryResponse
import com.example.foodplanner.data.model.IngredientResponse
import com.example.foodplanner.data.model.MealResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("search.php")
    fun searchMealByName(
        @Query("s") mealName: String
    ): Single<MealResponse>

    @GET("lookup.php")
    fun getMealDetails(
        @Query("i") mealId: String
    ): Single<MealResponse>

    @GET("random.php")
    fun getRandomMeal(): Single<MealResponse>

    @GET("categories.php")
    fun getCategories(): Single<CategoryResponse>

    @GET("list.php")
    fun getAreas(
        @Query("a") list: String = "list"
    ): Single<AreaResponse>

    @GET("list.php")
    fun getIngredients(
        @Query("i") list: String = "list"
    ): Single<IngredientResponse>

    @GET("filter.php")
    fun getMealsByCategory(
        @Query("c") category: String
    ): Single<MealResponse>

    @GET("filter.php")
    fun getMealsByArea(
        @Query("a") area: String
    ): Single<MealResponse>

    @GET("filter.php")
    fun getMealsByIngredient(
        @Query("i") ingredient: String
    ): Single<MealResponse>
}