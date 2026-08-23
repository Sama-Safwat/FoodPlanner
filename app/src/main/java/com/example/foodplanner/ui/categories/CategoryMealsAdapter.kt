package com.example.foodplanner.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.databinding.ItemMealBinding

class CategoryMealsAdapter(
    private val onMealClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryMealsAdapter.MealViewHolder>() {

    private var meals: List<Meal> = emptyList()

    fun submitList(list: List<Meal>) {
        meals = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    inner class MealViewHolder(
        private val binding: ItemMealBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.mealName.text = meal.strMeal ?: "Unknown"

            Glide.with(binding.root.context)
                .load(meal.strMealThumb)
                .into(binding.mealImage)

            binding.root.setOnClickListener {
                meal.idMeal?.let { onMealClick(it) }
            }
        }
    }
}