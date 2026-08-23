package com.example.foodplanner.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private val onItemClick: (MealEntity) -> Unit,
    private val onRemove: (MealEntity) -> Unit
) : ListAdapter<MealEntity, FavoritesAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MealEntity>() {
            override fun areItemsTheSame(oldItem: MealEntity, newItem: MealEntity) =
                oldItem.idMeal == newItem.idMeal

            override fun areContentsTheSame(oldItem: MealEntity, newItem: MealEntity) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: MealEntity) {
            binding.textMealName.text = meal.strMeal
            binding.textMealInfo.text = "${meal.strCategory} • ${meal.strArea}"

            Glide.with(binding.imageMeal.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.ic_meal_placeholder)
                .error(R.drawable.ic_meal_placeholder)
                .centerCrop()
                .into(binding.imageMeal)

            binding.root.setOnClickListener {
                onItemClick(meal)
            }

            binding.btnRemoveFavorite.setOnClickListener { onRemove(meal) }
        }
    }
}