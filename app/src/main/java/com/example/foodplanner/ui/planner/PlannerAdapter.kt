package com.example.foodplanner.ui.planner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.local.PlannedMealEntity

class PlannerAdapter(
    private val onDeleteClick: (PlannedMealEntity) -> Unit
) : RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder>() {

    private var meals: List<PlannedMealEntity> = emptyList()

    fun submitList(newList: List<PlannedMealEntity>) {
        meals = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_planner_meal, parent, false)
        return PlannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlannerViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    inner class PlannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgMeal: ImageView = itemView.findViewById(R.id.imgMeal)
        private val tvMealName: TextView = itemView.findViewById(R.id.tvMealName)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(meal: PlannedMealEntity) {
            tvMealName.text = meal.mealName
            Glide.with(itemView.context)
                .load(meal.mealImageUrl)
                .placeholder(R.drawable.ic_meal_placeholder)
                .into(imgMeal)

            btnDelete.setOnClickListener {
                onDeleteClick(meal)
            }
        }
    }
}