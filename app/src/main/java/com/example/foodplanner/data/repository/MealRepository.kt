package com.example.foodplanner.data.repository

import com.example.foodplanner.data.api.ApiService
import com.example.foodplanner.data.local.MealDao
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.data.model.Meal

class MealRepository(
    private val apiService: ApiService,
    private val mealDao: MealDao
) {

    // ---------- Remote ----------

    suspend fun searchMeals(name: String) =
        apiService.searchMeals(name)

    suspend fun getMealsByFirstLetter(letter: String) =
        apiService.getMealsByFirstLetter(letter)

    suspend fun getMealDetails(id: String) =
        apiService.getMealDetails(id)

    suspend fun getRandomMeal() =
        apiService.getRandomMeal()

    suspend fun getCategories() =
        apiService.getCategories()

    suspend fun getMealsByCategory(category: String) =
        apiService.getMealsByCategory(category)

    suspend fun getMealsByIngredient(ingredient: String) =
        apiService.getMealsByIngredient(ingredient)

    suspend fun getMealsByArea(area: String) =
        apiService.getMealsByArea(area)


    // ---------- Local ----------

    suspend fun addFavorite(meal: MealEntity) {
        mealDao.insertMeal(meal)
    }

    suspend fun removeFavorite(meal: MealEntity) {
        mealDao.deleteMeal(meal)
    }

    suspend fun getFavorites(): List<MealEntity> {
        return mealDao.getAllMeals()
    }

    suspend fun getFavoriteById(id: String): MealEntity? {
        return mealDao.getMealById(id)
    }
}