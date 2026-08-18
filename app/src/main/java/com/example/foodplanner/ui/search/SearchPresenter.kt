package com.example.foodplanner.ui.search

import com.example.foodplanner.data.repository.MealRemoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchPresenter(
    private val view: SearchContract.View,
    private val repository: MealRemoteRepository
) : SearchContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun start() {
        loadCategories()
        loadIngredients()
    }

    override fun stop() {
        disposables.clear()
    }

    override fun searchByName(query: String) {
        if (query.isEmpty()) {
            view.clearResults()
            return
        }
        view.showLoading()
        repository.searchMealsByName(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val meals = response.meals ?: emptyList()
                    if (meals.isNotEmpty()) {
                        view.showSearchResults(meals)
                    } else {
                        view.showError("No meals found")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError(error.message ?: "Search failed")
                }
            )
            .also { disposables.add(it) }
    }

    override fun searchByCategory(category: String) {
        if (category.isEmpty()) return
        view.showLoading()
        repository.getMealsByCategory(category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val meals = response.meals ?: emptyList()
                    if (meals.isNotEmpty()) {
                        view.showSearchResults(meals)
                    } else {
                        view.showError("No meals in this category")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError(error.message ?: "Failed to load meals")
                }
            )
            .also { disposables.add(it) }
    }

    override fun searchByIngredient(ingredient: String) {
        if (ingredient.isEmpty()) return
        view.showLoading()
        repository.getMealsByIngredient(ingredient)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val meals = response.meals ?: emptyList()
                    if (meals.isNotEmpty()) {
                        view.showSearchResults(meals)
                    } else {
                        view.showError("No meals with this ingredient")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError(error.message ?: "Failed to load meals")
                }
            )
            .also { disposables.add(it) }
    }

    override fun loadCategories() {
        repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    val categories = response.categories?.mapNotNull { it.strCategory } ?: emptyList()
                    view.showCategories(categories)
                },
                { error ->

                }
            )
            .also { disposables.add(it) }
    }

    override fun loadIngredients() {
        view.showLoading()
        repository.getIngredients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val ingredients = response.meals?.mapNotNull { it.strIngredient } ?: emptyList()
                    if (ingredients.isNotEmpty()) {
                        view.showIngredients(ingredients)
                    } else {
                        view.showError("No ingredients found from API")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError("Could not load ingredients. Please check your internet connection.")
                }
            )
            .also { disposables.add(it) }
    }

    override fun onMealClicked(mealId: String) {
        view.navigateToMealDetails(mealId)
    }
}