package com.example.foodplanner.ui.details

import com.example.foodplanner.base.BasePresenter
import com.example.foodplanner.base.BaseView
import com.example.foodplanner.data.model.Meal

interface MealDetailsContract {

    interface View : BaseView {
        fun showMealDetails(meal: Meal)
        fun showFavoriteStatus(isFavorite: Boolean)
        fun showVideo(videoUrl: String)
        fun showIngredients(ingredients: List<Pair<String, String>>)
        fun navigateBack()
    }

    interface Presenter : BasePresenter {
        fun loadMealDetails(mealId: String)
        fun toggleFavorite()
        fun isMealFavorite(): Boolean
        fun onBackPressed()
    }
}