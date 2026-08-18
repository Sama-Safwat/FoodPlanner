package com.example.foodplanner.ui.countries

import com.example.foodplanner.data.repository.MealRemoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CountriesPresenter(
    private val view: CountriesContract.View,
    private val repository: MealRemoteRepository
) : CountriesContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun start() {
        loadCountries()
    }

    override fun stop() {
        disposables.clear()
    }

    override fun loadCountries() {
        view.showLoading()

        repository.getAreas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val countries = response.meals ?: emptyList()
                    if (countries.isNotEmpty()) {
                        view.showCountries(countries)
                    } else {
                        view.showError("No countries found")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError("Error: ${error.message}")
                }
            )
            .also { disposables.add(it) }
    }

    override fun loadMealsByCountry(country: String) {
        view.showLoading()

        repository.getMealsByArea(country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val meals = response.meals ?: emptyList()
                    if (meals.isNotEmpty()) {
                        view.showCountryMeals(meals)
                    } else {
                        view.showError("No meals found in this country")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError(error.message ?: "Failed to load meals")
                }
            )
            .also { disposables.add(it) }
    }

    override fun onMealClicked(mealId: String) {
        view.navigateToMealDetails(mealId)
    }
}