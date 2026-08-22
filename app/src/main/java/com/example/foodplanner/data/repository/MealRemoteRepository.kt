package com.example.foodplanner.data.repository

import com.example.foodplanner.data.api.MealApi
import com.example.foodplanner.data.model.AreaResponse
import com.example.foodplanner.data.model.CategoryResponse
import com.example.foodplanner.data.model.IngredientResponse
import com.example.foodplanner.data.model.MealResponse
import io.reactivex.rxjava3.core.Single

class MealRemoteRepository(private val mealApi: MealApi) {

    fun getRandomMealRx(): Single<MealResponse> {
        return mealApi.getRandomMeal()
    }

    fun getMealDetailsRx(id: String): Single<MealResponse> {
        return mealApi.getMealDetails(id)
    }

    fun searchMealsByNameRx(name: String): Single<MealResponse> {
        return mealApi.searchMealByName(name)
    }

    fun searchMealsByFirstLetterRx(letter: String): Single<MealResponse> {
        return mealApi.searchMealsByFirstLetter(letter)
    }

    fun getCategoriesRx(): Single<CategoryResponse> {
        return mealApi.getCategories()
    }

    fun getMealsByCategoryRx(category: String): Single<MealResponse> {
        return mealApi.getMealsByCategory(category)
    }

    fun getMealsByIngredientRx(ingredient: String): Single<MealResponse> {
        return mealApi.getMealsByIngredient(ingredient)
    }

    fun getMealsByAreaRx(area: String): Single<MealResponse> {
        return mealApi.getMealsByArea(area)
    }

    fun getAreasRx(): Single<AreaResponse> {
        return mealApi.getAreas()
    }

    fun getIngredientsRx(): Single<IngredientResponse> {
        return mealApi.getIngredients()
    }

    suspend fun getRandomMeal(): MealResponse {
        return mealApi.getRandomMealSuspend()
    }

    suspend fun searchMealsByName(name: String): MealResponse {
        return mealApi.searchMealByNameSuspend(name)
    }

    suspend fun getMealDetails(id: String): MealResponse {
        return mealApi.getMealDetailsSuspend(id)
    }

    suspend fun searchMealsByFirstLetter(letter: String): MealResponse {
        return mealApi.searchMealsByFirstLetterSuspend(letter)
    }

    suspend fun getCategories(): CategoryResponse {
        return mealApi.getCategoriesSuspend()
    }

    suspend fun getMealsByCategory(category: String): MealResponse {
        return mealApi.getMealsByCategorySuspend(category)
    }

    suspend fun getMealsByIngredient(ingredient: String): MealResponse {
        return mealApi.getMealsByIngredientSuspend(ingredient)
    }

    suspend fun getMealsByArea(area: String): MealResponse {
        return mealApi.getMealsByAreaSuspend(area)
    }

    suspend fun getAreas(): AreaResponse {
        return mealApi.getAreasSuspend()
    }

    suspend fun getIngredients(): IngredientResponse {
        return mealApi.getIngredientsSuspend()
    }
}