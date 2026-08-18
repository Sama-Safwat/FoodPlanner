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
                    view.hideLoading()

                    val meal = response.meals?.firstOrNull()

                    if (meal != null) {
                        view.showMeal(meal)
                    } else {
                        view.showError("No meal found")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError(
                        error.message ?: "Something went wrong"
                    )
                }
            )
            .also { disposables.add(it) }
    }
}