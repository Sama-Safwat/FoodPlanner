package com.example.foodplanner.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.bumptech.glide.Glide
import com.example.foodplanner.databinding.ItemIngredientBinding

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private var ingredients: List<Pair<String, String>> = emptyList()

    fun submitList(list: List<Pair<String, String>>) {
        ingredients = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int = ingredients.size

    inner class IngredientViewHolder(
        private val binding: ItemIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Pair<String, String>) {
            binding.ingredientName.text = ingredient.first
            binding.ingredientMeasure.text = ingredient.second.ifEmpty { "To taste" }

            val imageUrl = "https://www.themealdb.com/images/ingredients/${ingredient.first}-small.png"
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_meal_placeholder)
                .error(R.drawable.ic_meal_placeholder)
                .into(binding.ingredientImage)
        }
    }
}