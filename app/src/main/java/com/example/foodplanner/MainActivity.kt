package com.example.foodplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.ui.auth.SplashFragment
import com.example.foodplanner.ui.categories.CategoriesFragment
import com.example.foodplanner.ui.countries.CountriesFragment
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.example.foodplanner.ui.favorites.FavoritesActivity
import com.example.foodplanner.ui.home.HomeFragment
import com.example.foodplanner.ui.planner.PlannerActivity
import com.example.foodplanner.ui.search.SearchFragment
import com.example.foodplanner.utils.AuthGuard

// MainActivity.kt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navContainer = findViewById<HorizontalScrollView>(R.id.navContainer)
        navContainer?.visibility = View.GONE
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SplashFragment())
                .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (currentFragment is HomeFragment) {
                navContainer?.visibility = View.VISIBLE
            } else {
                navContainer?.visibility = View.GONE
            }
        }

        // أزرار للـ Testing
        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnCategories = findViewById<Button>(R.id.btnCategories)
        val btnCountries = findViewById<Button>(R.id.btnCountries)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnDetails = findViewById<Button>(R.id.btnDetails)
        val btnFavorites = findViewById<Button>(R.id.btnFavorites)
        val btnPlanner = findViewById<Button>(R.id.btnPlanner)

        btnHome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        btnCategories.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CategoriesFragment())
                .commit()
        }

        btnCountries.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CountriesFragment())
                .commit()
        }

        btnSearch.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SearchFragment())
                .commit()
        }

        btnDetails.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MealDetailsFragment.newInstance("52772"))
                .commit()
        }

        val userPrefs = UserPreferences(this)
        btnFavorites?.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (currentFragment != null) {
                AuthGuard.requireLogin(currentFragment, userPrefs) {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                }
            } else {
                if (!userPrefs.isGuest()) {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                }
            }
        }

        btnPlanner?.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (currentFragment != null) {
                AuthGuard.requireLogin(currentFragment, userPrefs) {
                    startActivity(Intent(this, PlannerActivity::class.java))
                }
            } else {
                if (!userPrefs.isGuest()) {
                    startActivity(Intent(this, PlannerActivity::class.java))
                }
            }
        }
    }
}