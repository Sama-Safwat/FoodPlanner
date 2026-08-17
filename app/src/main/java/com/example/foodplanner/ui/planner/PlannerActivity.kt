package com.example.foodplanner.ui.planner

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.data.local.AppDatabase
import com.example.foodplanner.data.local.PlannedMealEntity
import com.example.foodplanner.data.repository.WeeklyPlanRepository
import com.example.foodplanner.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Date

class PlannerActivity : AppCompatActivity() {

    private lateinit var calendarPlanner: CalendarView
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnAddMeal: Button
    private lateinit var recyclerPlanner: RecyclerView
    private lateinit var adapter: PlannerAdapter
    private lateinit var repository: WeeklyPlanRepository


    private var selectedDate = DateUtils.toIso(Date())
    private var allMeals = emptyList<PlannedMealEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        calendarPlanner = findViewById(R.id.calendarPlanner)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        btnAddMeal = findViewById(R.id.btnAddMeal)
        recyclerPlanner = findViewById(R.id.recyclerPlanner)

        val database = AppDatabase.getDatabase(this)
        repository = WeeklyPlanRepository(database.planDao())

        adapter = PlannerAdapter { meal ->
            lifecycleScope.launch {
                repository.removeMealFromPlan(meal.planId)
                Toast.makeText(
                    this@PlannerActivity,
                    "Meal removed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        recyclerPlanner.layoutManager = LinearLayoutManager(this)
        recyclerPlanner.adapter = adapter


        tvSelectedDate.text = "Meals for ${DateUtils.toDisplay(selectedDate)}"

        observePlan()

        calendarPlanner.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendarDate = java.util.Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }


            selectedDate = DateUtils.toIso(calendarDate.time)


            tvSelectedDate.text = "Meals for ${DateUtils.toDisplay(selectedDate)}"

            showSelectedDayMeals()
        }

        btnAddMeal.setOnClickListener {
            addTestMeal()
        }
    }

    private fun observePlan() {
        lifecycleScope.launch {
            repository.getPlan().collect { meals ->
                allMeals = meals
                showSelectedDayMeals()
            }
        }
    }

    private fun showSelectedDayMeals() {

        val selectedMeals = allMeals.filter { it.date == selectedDate }
        adapter.submitList(selectedMeals)
    }

    private fun addTestMeal() {
        val testMeal = PlannedMealEntity(
            date = selectedDate,
            mealId = "test_${System.currentTimeMillis()}",
            mealName = "Chicken Curry",
            mealImageUrl = "https://www.themealdb.com/images/media/meals/wyxwsp1486979827.jpg"
        )

        lifecycleScope.launch {
            repository.addMealToPlan(testMeal)
            Toast.makeText(
                this@PlannerActivity,
                "Meal added to ${DateUtils.toDisplay(selectedDate)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}