package com.example.foodplanner.ui.planner

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.R
import com.example.foodplanner.data.local.AppDatabase
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.data.repository.WeeklyPlanRepository
import com.example.foodplanner.utils.DateUtils

class PlannerActivity : AppCompatActivity() {

    private lateinit var calendarPlanner: CalendarView
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnAddMeal: Button
    private lateinit var recyclerPlanner: RecyclerView
    private lateinit var adapter: PlannerAdapter
    private lateinit var viewModel: PlannerViewModel

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
        val repository = WeeklyPlanRepository(database.planDao())

        val factory = PlannerViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PlannerViewModel::class.java)

        setupAdapter()
        setupObservers()
        setupListeners()

        tvSelectedDate.text = "Meals for ${DateUtils.toDisplay(viewModel.selectedDate.value ?: "")}"
    }

    private fun setupAdapter() {
        adapter = PlannerAdapter { meal ->
            viewModel.removeMealFromPlan(meal)
        }

        recyclerPlanner.layoutManager = LinearLayoutManager(this)
        recyclerPlanner.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.meals.observe(this) { meals ->
            adapter.submitList(meals)
        }

        viewModel.selectedDate.observe(this) { date ->
            tvSelectedDate.text = "Meals for ${DateUtils.toDisplay(date)}"
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
    }

    private fun setupListeners() {
        calendarPlanner.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendarDate = java.util.Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val date = DateUtils.toIso(calendarDate.time)
            viewModel.onDateSelected(date)
        }

        btnAddMeal.setOnClickListener {
            if (incomingMealId != null && incomingMealName != null) {
                viewModel.addMealToPlan(
                    incomingMealId!!,
                    incomingMealName!!,
                    incomingMealImage
                )
                incomingMealId = null
                incomingMealName = null
                incomingMealImage = null
            } else {
                Toast.makeText(
                    this,
                    "No meal selected. Please go back and choose a meal first.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (incomingMealId != null && incomingMealName != null) {
            Toast.makeText(
                this,
                "Add '${incomingMealName}' to plan? Press 'Add Meal'",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}