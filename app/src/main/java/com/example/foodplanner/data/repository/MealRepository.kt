package com.example.foodplanner.data.repository

import android.util.Log
import com.example.foodplanner.App
import com.example.foodplanner.data.api.MealApi
import com.example.foodplanner.data.local.MealDao
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.data.sync.SyncManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class MealRepository(
    private val mealApi: MealApi,
    private val mealDao: MealDao,
    private val sync: SyncManager? = App.instance.syncManager
) {

    fun getRandomMeal() = mealApi.getRandomMeal()
    fun getMealDetails(id: String) = mealApi.getMealDetails(id)
    fun searchMealsByName(name: String) = mealApi.searchMealByName(name)
    fun getCategories() = mealApi.getCategories()
    fun getMealsByCategory(category: String) = mealApi.getMealsByCategory(category)
    fun getMealsByIngredient(ingredient: String) = mealApi.getMealsByIngredient(ingredient)
    fun getMealsByArea(area: String) = mealApi.getMealsByArea(area)
    fun getAreas() = mealApi.getAreas()

    fun isMealFavorite(userId: String, mealId: String): Single<Boolean> {
        return Single.fromCallable {
            mealDao.getMealByIdSync(userId, mealId) != null
        }
    }

    fun addFavorite(mealEntity: MealEntity): Completable {
        return Completable.fromAction {
            Log.d("SYNC", "addFavorite → meal=${mealEntity.idMeal}, syncConnected=${sync != null}")
            mealDao.insertMeal(mealEntity)
            sync?.backupFavorite(mealEntity)
        }
    }

    fun removeFavorite(userId: String, mealId: String): Completable {
        return Completable.fromAction {
            mealDao.deleteMealById(userId, mealId)
            sync?.removeFavoriteBackup(mealId)
        }
    }

    fun getFavorites(userId: String): Single<List<MealEntity>> {
        return Single.fromCallable {
            mealDao.getAllMealsForUser(userId)
        }
    }

    suspend fun isMealFavoriteSuspend(userId: String, mealId: String): Boolean {
        return mealDao.getMealById(userId, mealId) != null
    }

    suspend fun addFavoriteSuspend(mealEntity: MealEntity) {
        mealDao.insertMeal(mealEntity)
    }

    suspend fun removeFavoriteSuspend(userId: String, mealId: String) {
        mealDao.deleteMealById(userId, mealId)
    }


}