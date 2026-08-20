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
import com.example.foodplanner.data.repository.UserPreferences
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

    private var incomingMealId: String? = null
    private var incomingMealName: String? = null
    private var incomingMealImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPrefs = UserPreferences(this)
        if (userPrefs.isGuest()) {
            Toast.makeText(this, "Please log in to use this feature", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        setContentView(R.layout.activity_planner)

        calendarPlanner = findViewById(R.id.calendarPlanner)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        btnAddMeal = findViewById(R.id.btnAddMeal)
        recyclerPlanner = findViewById(R.id.recyclerPlanner)

        incomingMealId = intent.getStringExtra("meal_id")
        incomingMealName = intent.getStringExtra("meal_name")
        incomingMealImage = intent.getStringExtra("meal_image")

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
            if (incomingMealId != null && incomingMealName != null){
                addMealToPlan(incomingMealId!!, incomingMealName!!, incomingMealImage!!)
                incomingMealId = null
                incomingMealName = null
                incomingMealImage = null
            }else{
                Toast.makeText(
                    this,
                    "No meal selected. please go back and choose meal first.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (incomingMealId != null && incomingMealName != null){
            Toast.makeText(
                this,
                "Add '${incomingMealName}' to plan? Press 'Add Meal' ",
                Toast.LENGTH_LONG
            ).show()
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

    private fun addMealToPlan(mealId: String, mealName: String, mealImageUrl: String?){
        val meal = PlannedMealEntity(
            userId = "",
            date = selectedDate,
            mealId = mealId,
            mealName = mealName,
            mealImageUrl = mealImageUrl ?: "https://www.themealdb.com/images/media/meals/placeholder.jpg"
        )
        lifecycleScope.launch {
            repository.addMealToPlan(meal)
            Toast.makeText(
                this@PlannerActivity,
                "'${mealName}' added to ${DateUtils.toDisplay(selectedDate)}",
                Toast.LENGTH_SHORT
            ).show()

            incomingMealId = null
            incomingMealName = null
            incomingMealImage = null
        }
    }


}