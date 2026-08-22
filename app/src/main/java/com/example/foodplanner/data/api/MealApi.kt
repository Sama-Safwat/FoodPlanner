package com.example.foodplanner.data.api

import com.example.foodplanner.data.model.AreaResponse
import com.example.foodplanner.data.model.CategoryResponse
import com.example.foodplanner.data.model.IngredientResponse
import com.example.foodplanner.data.model.MealResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Single<MealResponse>

    @GET("search.php")
    fun searchMealByName(
        @Query("s") mealName: String
    ): Single<MealResponse>

    @GET("lookup.php")
    fun getMealDetails(
        @Query("i") mealId: String
    ): Single<MealResponse>

    @GET("search.php")
    fun searchMealsByFirstLetter(
        @Query("f") letter: String
    ): Single<MealResponse>

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

    @GET("random.php")
    suspend fun getRandomMealSuspend(): MealResponse

    @GET("search.php")
    suspend fun searchMealByNameSuspend(
        @Query("s") mealName: String
    ): MealResponse

    @GET("lookup.php")
    suspend fun getMealDetailsSuspend(
        @Query("i") mealId: String
    ): MealResponse

    @GET("search.php")
    suspend fun searchMealsByFirstLetterSuspend(
        @Query("f") letter: String
    ): MealResponse

    @GET("categories.php")
    suspend fun getCategoriesSuspend(): CategoryResponse

    @GET("list.php")
    suspend fun getAreasSuspend(
        @Query("a") list: String = "list"
    ): AreaResponse

    @GET("list.php")
    suspend fun getIngredientsSuspend(
        @Query("i") list: String = "list"
    ): IngredientResponse

    @GET("filter.php")
    suspend fun getMealsByCategorySuspend(
        @Query("c") category: String
    ): MealResponse

    @GET("filter.php")
    suspend fun getMealsByAreaSuspend(
        @Query("a") area: String
    ): MealResponse

    @GET("filter.php")
    suspend fun getMealsByIngredientSuspend(
        @Query("i") ingredient: String
    ): MealResponse
}