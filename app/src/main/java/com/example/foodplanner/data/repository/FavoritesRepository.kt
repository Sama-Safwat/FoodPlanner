package com.example.foodplanner.data.repository

import com.example.foodplanner.data.local.FavoritesDao
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.utils.UserProvider
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(
    private val dao: FavoritesDao
) {

    private fun userId(): String = UserProvider.getCurrentUserId()

    fun getFavorites(): Flow<List<MealEntity>> {
        return dao.getAllFavorites(userId())
    }

    suspend fun addFavorite(meal: MealEntity) {
        dao.addFavorite(meal.copy(userId = userId()))
    }

    suspend fun removeFavorite(meal: MealEntity) {
        dao.removeFavorite(meal)
    }

    suspend fun removeFavoriteById(mealId: String) {
        dao.removeFavoriteById(userId(), mealId)
    }

    suspend fun isFavorite(mealId: String): Boolean {
        return dao.isFavorite(userId(), mealId)
    }
}