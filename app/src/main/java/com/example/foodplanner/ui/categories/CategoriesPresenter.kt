package com.example.foodplanner.ui.categories

import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoriesPresenter(
    private val view: CategoriesContract.View,
    private val repository: MealRemoteRepository
) : CategoriesContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun start() {
        loadCategories()
    }

    override fun stop() {
        disposables.clear()
    }

    override fun loadCategories() {
        view.showLoading()

        repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val categories = response.categories ?: emptyList()
                    if (categories.isNotEmpty()) {
                        view.showCategories(categories)
                    } else {
                        view.showError("No categories found")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError("Error: ${error.message}")
                }
            )
            .also { disposables.add(it) }
    }

    override fun loadMealsByCategory(category: String) {
        view.showLoading()

        repository.getMealsByCategory(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val meals = response.meals ?: emptyList()
                    if (meals.isNotEmpty()) {
                        view.showCategoryMeals(meals)
                    } else {
                        view.showError("No meals found in this category")
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