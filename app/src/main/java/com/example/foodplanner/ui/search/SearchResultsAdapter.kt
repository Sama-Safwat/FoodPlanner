package com.example.foodplanner.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.databinding.ItemMealBinding

class SearchResultsAdapter(
    private val onMealClick: (String) -> Unit
) : RecyclerView.Adapter<SearchResultsAdapter.MealViewHolder>() {

    private var items: List<Any> = emptyList()

    fun submitList(list: List<Any>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is Meal -> holder.bindMeal(item)
            is String -> holder.bindIngredient(item)
        }
    }

    override fun getItemCount(): Int = items.size

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

            }
        }
    }
}