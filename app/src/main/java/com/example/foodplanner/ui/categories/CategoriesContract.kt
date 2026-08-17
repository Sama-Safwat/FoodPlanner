package com.example.foodplanner.ui.categories

import com.example.foodplanner.base.BasePresenter
import com.example.foodplanner.base.BaseView
import com.example.foodplanner.data.model.Category
import com.example.foodplanner.data.model.Meal

interface CategoriesContract {

    interface View : BaseView {
        fun showCategories(categories: List<Category>)
        fun showCategoryMeals(meals: List<Meal>)
        fun navigateToMealDetails(mealId: String)
    }

    interface Presenter : BasePresenter {
        fun loadCategories()
        fun loadMealsByCategory(category: String)
        fun onMealClicked(mealId: String)
    }
}