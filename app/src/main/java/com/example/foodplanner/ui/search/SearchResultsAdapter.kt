package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.model.Ingredient
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.databinding.ItemMealBinding

class SearchResultsAdapter(
    private val onMealClick: (String) -> Unit,
    private val onIngredientClick: ((String) -> Unit)? = null
) : RecyclerView.Adapter<SearchResultsAdapter.MealViewHolder>() {

    private var meals: List<Meal> = emptyList()
    private var ingredients: List<String> = emptyList()
    private var isIngredientsMode = false

    fun submitMeals(list: List<Meal>) {
        meals = list
        ingredients = emptyList()
        isIngredientsMode = false
        notifyDataSetChanged()
    }

    fun submitIngredients(list: List<String>) {
        ingredients = list
        meals = emptyList()
        isIngredientsMode = true
        notifyDataSetChanged()
    }

    fun clear() {
        meals = emptyList()
        ingredients = emptyList()
        isIngredientsMode = false
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        if (isIngredientsMode) {
            holder.bindIngredient(ingredients[position])
        } else {
            holder.bindMeal(meals[position])
        }
    }

    override fun getItemCount(): Int {
        return if (isIngredientsMode) ingredients.size else meals.size
    }

    inner class MealViewHolder(
        private val binding: ItemMealBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindMeal(meal: Meal) {
            binding.mealName.text = meal.strMeal ?: "Unknown"
            Glide.with(binding.root.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.mealImage)
            binding.root.setOnClickListener {
                meal.idMeal?.let { onMealClick(it) }
            }
        }

        fun bindIngredient(ingredient: String) {
            binding.mealName.text = ingredient
            Glide.with(binding.root.context)
                .load("https://www.themealdb.com/images/ingredients/${ingredient}-small.png")
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.mealImage)
            binding.root.setOnClickListener {
                onIngredientClick?.invoke(ingredient)
                }
            }
        }
    }
