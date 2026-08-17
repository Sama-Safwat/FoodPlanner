package com.example.foodplanner.ui.countries

import com.example.foodplanner.base.BasePresenter
import com.example.foodplanner.base.BaseView
import com.example.foodplanner.data.model.Area
import com.example.foodplanner.data.model.Meal

interface CountriesContract {

    interface View : BaseView {
        fun showCountries(countries: List<Area>)
        fun showCountryMeals(meals: List<Meal>)
        fun navigateToMealDetails(mealId: String)
    }

    interface Presenter : BasePresenter {
        fun loadCountries()
        fun loadMealsByCountry(country: String)
        fun onMealClicked(mealId: String)
    }
}