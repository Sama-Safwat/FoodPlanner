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

    // add userId as a parameter
    fun isMealFavorite(userId: String, mealId: String): Single<Boolean> {
        return Single.fromCallable {
            mealDao.getMealByIdSync(userId, mealId) != null
        }
    }


    fun addFavorite(mealEntity: MealEntity): Completable {
        return Completable.fromAction {
            mealDao.insertMeal(mealEntity)
        }
    }

    // add userID as a parameter
    fun removeFavorite(userId: String, mealId: String): Completable {
        return Completable.fromAction {
            mealDao.deleteMealById(userId, mealId)
        }
    }

    // add userId as a parameter
    fun getFavorites(userId: String): Single<List<MealEntity>> {
        return Single.fromCallable {
            mealDao.getAllMealsForUser(userId)
        }
    }
}