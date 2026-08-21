package com.example.foodplanner.ui.home

import com.example.foodplanner.data.repository.MealRemoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MealPresenter(
    private val view: MealContract.View,
    private val repository: MealRemoteRepository
) : MealContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun start() {
        loadMealOfTheDay()
        loadMeals()
    }

    override fun stop() {
        disposables.clear()
    }

    override fun loadMealOfTheDay() {

        view.showLoading()

        repository.getRandomMeal()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    val meal = response.meals?.firstOrNull()

                    if (meal != null) {
                        view.showMeal(meal)
                    }
                },
                {
                    view.showError("Unable to load meal of the day")
                }
            )
            .also {
                disposables.add(it)
            }
    }

    override fun loadMeals() {

        repository.searchMealsByName("a")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    val meals = response.meals.orEmpty()

                    if (meals.isNotEmpty()) {
                        view.showMeals(meals.take(10))
                    }
                },
                {
                    // Meal of the day can still work even if this request fails
                }
            )
            .also {
                disposables.add(it)
            }
    }
}