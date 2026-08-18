package com.example.foodplanner.data.repository

import com.example.foodplanner.data.api.MealApi
import com.example.foodplanner.data.local.MealDao
import com.example.foodplanner.data.local.MealEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class MealRepository(
    private val mealApi: MealApi,
    private val mealDao: MealDao
) {

    fun getRandomMeal() = mealApi.getRandomMeal()
    fun getMealDetails(id: String) = mealApi.getMealDetails(id)
    fun searchMealsByName(name: String) = mealApi.searchMealByName(name)
    fun getCategories() = mealApi.getCategories()
    fun getMealsByCategory(category: String) = mealApi.getMealsByCategory(category)
    fun getMealsByIngredient(ingredient: String) = mealApi.getMealsByIngredient(ingredient)
    fun getMealsByArea(area: String) = mealApi.getMealsByArea(area)
    fun getAreas() = mealApi.getAreas()

    fun isMealFavorite(mealId: String): Single<Boolean> {
        return Single.fromCallable {
            mealDao.getMealById(mealId) != null
        }
    }

    fun addFavorite(mealEntity: MealEntity): Completable {
        return Completable.fromAction {
            mealDao.insertMeal(mealEntity)
        }
    }

    fun removeFavorite(mealId: String): Completable {
        return Completable.fromAction {
            mealDao.deleteMealById(mealId)
        }
    }

    fun getFavorites(): Single<List<MealEntity>> {
        return Single.fromCallable {
            mealDao.getAllMeals()
        }
    }
}