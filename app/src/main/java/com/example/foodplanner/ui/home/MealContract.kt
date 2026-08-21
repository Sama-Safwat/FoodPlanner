package com.example.foodplanner.ui.home

import com.example.foodplanner.base.BasePresenter
import com.example.foodplanner.base.BaseView
import com.example.foodplanner.data.model.Meal

interface MealContract {

    interface View : BaseView {
        fun showMeal(meal: Meal)
        fun showMeals(meals: List<Meal>)
    }

    interface Presenter : BasePresenter {
        fun loadMealOfTheDay()
        fun loadMeals()
    }
}