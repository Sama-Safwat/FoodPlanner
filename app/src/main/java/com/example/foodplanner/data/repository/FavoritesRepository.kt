package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.FavoritesDao
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.utils.UserProvider
import kotlinx.coroutines.flow.Flow
import com.example.foodplanner.App
import com.example.foodplanner.data.sync.SyncManager
class FavoritesRepository(
    private val dao: FavoritesDao,
    private val sync: SyncManager? = App.instance.syncManager
) {

    private fun userId(): String = UserProvider.getCurrentUserId()

    fun getFavorites(): Flow<List<MealEntity>> {
        return dao.getAllFavorites(userId())

    }

    suspend fun addFavorite(meal: MealEntity) {
        val withUser = meal.copy(userId = userId())
        dao.addFavorite(withUser)
        sync?.backupFavorite(withUser)
    }

    suspend fun removeFavorite(meal: MealEntity) {
        dao.removeFavorite(meal)
        sync?.removeFavoriteBackup(meal.idMeal)
    }

    suspend fun removeFavoriteById(mealId: String) {
        dao.removeFavoriteById(userId(), mealId)
    }

    suspend fun isFavorite(mealId: String): Boolean {
        return dao.isFavorite(userId(), mealId)
    }
}