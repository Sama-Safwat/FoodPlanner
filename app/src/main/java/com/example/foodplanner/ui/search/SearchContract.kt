package com.example.foodplanner.ui.search

import com.example.foodplanner.base.BasePresenter
import com.example.foodplanner.base.BaseView
import com.example.foodplanner.data.model.Meal

interface SearchContract {

    interface View : BaseView {
        fun showSearchResults(meals: List<Meal>)
        fun showCategories(categories: List<String>)
        fun showIngredients(ingredients: List<String>)
        fun navigateToMealDetails(mealId: String)
        fun clearResults()
    }

    interface Presenter : BasePresenter {
        fun searchByName(query: String)
        fun searchByCategory(category: String)
        fun searchByIngredient(ingredient: String)
        fun loadCategories()
        fun loadIngredients()
        fun onMealClicked(mealId: String)
    }
}