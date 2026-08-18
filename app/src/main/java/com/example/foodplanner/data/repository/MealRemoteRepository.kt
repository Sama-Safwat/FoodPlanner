package com.example.foodplanner.data.repository

import com.example.foodplanner.data.api.MealApi
import com.example.foodplanner.data.model.AreaResponse
import com.example.foodplanner.data.model.CategoryResponse
import com.example.foodplanner.data.model.IngredientResponse
import com.example.foodplanner.data.model.MealResponse
import io.reactivex.rxjava3.core.Single

class MealRemoteRepository(private val mealApi: MealApi) {

    fun getRandomMeal(): Single<MealResponse> {
        return mealApi.getRandomMeal()
    }

    fun getMealDetails(id: String): Single<MealResponse> {
        return mealApi.getMealDetails(id)
    }

    fun searchMealsByName(name: String): Single<MealResponse> {
        return mealApi.searchMealByName(name)
    }

    fun getCategories(): Single<CategoryResponse> {
        return mealApi.getCategories()
    }

    fun getMealsByCategory(category: String): Single<MealResponse> {
        return mealApi.getMealsByCategory(category)
    }

    fun getMealsByIngredient(ingredient: String): Single<MealResponse> {
        return mealApi.getMealsByIngredient(ingredient)
    }

    fun getMealsByArea(area: String): Single<MealResponse> {
        return mealApi.getMealsByArea(area)
    }

    fun getAreas(): Single<AreaResponse> {
        return mealApi.getAreas()
    }

    fun getIngredients(): Single<IngredientResponse>{
        return mealApi.getIngredients()
    }
}