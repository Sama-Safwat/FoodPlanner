package com.example.foodplanner.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.model.Meal

class HomeMealAdapter(
    private val onMealClick: (Meal) -> Unit
) : RecyclerView.Adapter<HomeMealAdapter.MealViewHolder>() {

    private val meals = mutableListOf<Meal>()

    fun submitList(newMeals: List<Meal>) {
        meals.clear()
        meals.addAll(newMeals)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_meal, parent, false)

        return MealViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MealViewHolder,
        position: Int
    ) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    inner class MealViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val imgMeal: ImageView =
            itemView.findViewById(R.id.imgMeal)

        private val tvMealName: TextView =
            itemView.findViewById(R.id.tvMealName)

        private val tvMealCountry: TextView =
            itemView.findViewById(R.id.tvMealCountry)

        fun bind(meal: Meal) {

            tvMealName.text = meal.strMeal ?: "Unknown meal"
            tvMealCountry.text = meal.strArea ?: "Unknown country"

            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .centerCrop()
                .into(imgMeal)

            itemView.setOnClickListener {
                onMealClick(meal)
            }
        }
    }
}