package com.example.foodplanner.ui.details

import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.data.repository.MealRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import com.example.foodplanner.utils.UserProvider

class MealDetailsPresenter(
    private val view: MealDetailsContract.View,
    private val remoteRepository: MealRemoteRepository,
    private val localRepository: MealRepository
) : MealDetailsContract.Presenter {

    private val disposables = CompositeDisposable()
    private var currentMeal: Meal? = null
    private var isFavorite = false

    override fun start() {
    }

    override fun stop() {
        disposables.clear()
    }

    override fun loadMealDetails(mealId: String) {
        view.showLoading()

        remoteRepository.getMealDetails(mealId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    view.hideLoading()
                    val meal = response.meals?.firstOrNull()
                    if (meal != null) {
                        currentMeal = meal
                        view.showMealDetails(meal)
                        view.showIngredients(extractIngredients(meal))
                        checkFavoriteStatus(mealId)

                        meal.strYoutube?.let { videoUrl ->
                            if (videoUrl.isNotEmpty()) {
                                view.showVideo(videoUrl)
                            }
                        }
                    } else {
                        view.showError("Meal not found")
                    }
                },
                { error ->
                    view.hideLoading()
                    view.showError(error.message ?: "Failed to load meal details")
                }
            )
            .also { disposables.add(it) }
    }

    private fun checkFavoriteStatus(mealId: String) {
        // ✅ ضيفنا userId للـ repository
        localRepository.isMealFavorite(UserProvider.getCurrentUserId(), mealId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isFav ->
                    isFavorite = isFav
                    view.showFavoriteStatus(isFav)
                },
                { error ->
                    isFavorite = false
                    view.showFavoriteStatus(false)
                }
            )
            .also { disposables.add(it) }
    }

    override fun toggleFavorite() {
        if (currentMeal == null) return

        isFavorite = !isFavorite
        view.showFavoriteStatus(isFavorite)

        if (isFavorite) {
            currentMeal?.let { meal ->
                val mealEntity = MealEntity(
                    userId = UserProvider.getCurrentUserId(),   // ✅ الجديد
                    idMeal = meal.idMeal ?: "",
                    strMeal = meal.strMeal,
                    strCategory = meal.strCategory,
                    strArea = meal.strArea,
                    strInstructions = meal.strInstructions,
                    strMealThumb = meal.strMealThumb,
                    strYoutube = meal.strYoutube
                )
                localRepository.addFavorite(mealEntity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                        },
                        { error ->
                            view.showError("Failed to add to favorites")
                            isFavorite = false
                            view.showFavoriteStatus(false)
                        }
                    )
                    .also { disposables.add(it) }
            }
        } else {
            currentMeal?.let { meal ->
                meal.idMeal?.let { id ->
                    // ✅ ضيفنا userId
                    localRepository.removeFavorite(UserProvider.getCurrentUserId(), id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                            },
                            { error ->
                                view.showError("Failed to remove from favorites")
                                isFavorite = true
                                view.showFavoriteStatus(true)
                            }
                        )
                        .also { disposables.add(it) }
                }
            }
        }
    }

    override fun isMealFavorite(): Boolean = isFavorite

    override fun onBackPressed() {
        view.navigateBack()
    }

    private fun extractIngredients(meal: Meal): List<Pair<String, String>> {
        val ingredients = mutableListOf<Pair<String, String>>()

        val ingredientFields = listOf(
            meal.strIngredient1, meal.strIngredient2, meal.strIngredient3,
            meal.strIngredient4, meal.strIngredient5, meal.strIngredient6,
            meal.strIngredient7, meal.strIngredient8, meal.strIngredient9,
            meal.strIngredient10, meal.strIngredient11, meal.strIngredient12,
            meal.strIngredient13, meal.strIngredient14, meal.strIngredient15,
            meal.strIngredient16, meal.strIngredient17, meal.strIngredient18,
            meal.strIngredient19, meal.strIngredient20
        )

        val measureFields = listOf(
            meal.strMeasure1, meal.strMeasure2, meal.strMeasure3,
            meal.strMeasure4, meal.strMeasure5, meal.strMeasure6,
            meal.strMeasure7, meal.strMeasure8, meal.strMeasure9,
            meal.strMeasure10, meal.strMeasure11, meal.strMeasure12,
            meal.strMeasure13, meal.strMeasure14, meal.strMeasure15,
            meal.strMeasure16, meal.strMeasure17, meal.strMeasure18,
            meal.strMeasure19, meal.strMeasure20
        )

        for (i in ingredientFields.indices) {
            val ingredient = ingredientFields[i]
            val measure = measureFields.getOrNull(i)

            if (!ingredient.isNullOrEmpty() && !ingredient.isBlank()) {
                val measureText = if (!measure.isNullOrEmpty() && !measure.isBlank()) {
                    measure
                } else {
                    ""
                }
                ingredients.add(Pair(ingredient, measureText))
            }
        }

        return ingredients
    }
}