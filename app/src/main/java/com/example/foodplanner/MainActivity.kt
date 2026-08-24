package com.example.foodplanner

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import android.view.View
import androidx.fragment.app.Fragment
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.ui.auth.LoginFragment
import com.example.foodplanner.ui.auth.RegisterFragment
import com.example.foodplanner.ui.auth.SplashFragment
import com.example.foodplanner.ui.categories.CategoriesFragment
import com.example.foodplanner.ui.countries.CountriesFragment
import com.example.foodplanner.ui.favorites.FavoritesActivity
import com.example.foodplanner.ui.home.HomeFragment
import com.example.foodplanner.ui.planner.PlannerActivity
import com.example.foodplanner.ui.search.SearchFragment
import com.example.foodplanner.utils.AuthGuard
import com.example.foodplanner.utils.UserProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        toolbar = findViewById(R.id.toolbar)

        toolbar.visibility = View.GONE
        bottomNavigation.visibility = View.GONE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        setupToolbar()
        setupBottomNavigation()
        setupDrawer()
        observeFragmentChanges()

        if (savedInstanceState == null) {
            openFragment(SplashFragment())
        }
        val userPrefs = UserPreferences(this)
        if (userPrefs.isLoggedIn()) {
            lifecycleScope.launch { App.instance.syncManager.restore()
            } }
    }

    private fun observeFragmentChanges() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: androidx.fragment.app.FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    val hideChrome = f is SplashFragment || f is LoginFragment || f is RegisterFragment
                    toolbar.visibility = if (hideChrome) View.GONE else View.VISIBLE
                    bottomNavigation.visibility = if (hideChrome) View.GONE else View.VISIBLE
                    drawerLayout.setDrawerLockMode(
                        if (hideChrome) DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                        else DrawerLayout.LOCK_MODE_UNLOCKED
                    )
                }
            },
            false
        )
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.nav_categories -> {
                    openFragment(CategoriesFragment())
                    true
                }
                R.id.nav_countries -> {
                    openFragment(CountriesFragment())
                    true
                }
                R.id.nav_planner -> {
                    val userPrefs = UserPreferences(this)
                    val currentFragment = supportFragmentManager.findFragmentById(
                        R.id.fragmentContainer
                    ) ?: HomeFragment()

                    AuthGuard.requireLogin(
                        currentFragment,
                        userPrefs
                    ) {
                        startActivity(Intent(this, PlannerActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.drawer_home -> {
                    openFragment(HomeFragment())
                    bottomNavigation.selectedItemId = R.id.nav_home
                }
                R.id.drawer_favorites -> {
                    val userPrefs = UserPreferences(this)
                    val currentFragment = supportFragmentManager.findFragmentById(
                        R.id.fragmentContainer
                    ) ?: HomeFragment()

                    AuthGuard.requireLogin(
                        currentFragment,
                        userPrefs
                    ) {
                        startActivity(Intent(this, FavoritesActivity::class.java))
                    }
                }
                R.id.drawer_planner -> {
                    val userPrefs = UserPreferences(this)
                    val currentFragment = supportFragmentManager.findFragmentById(
                        R.id.fragmentContainer
                    ) ?: HomeFragment()

                    AuthGuard.requireLogin(
                        currentFragment,
                        userPrefs
                    ) {
                        startActivity(Intent(this, PlannerActivity::class.java))
                    }
                }
                R.id.drawer_search -> {
                    openFragment(SearchFragment())
                }
                R.id.drawer_logout -> {
                    UserProvider.logout()
                    UserPreferences(this).clearSession()
                    supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    openFragment(LoginFragment())
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun openSearch() {
        openFragment(SearchFragment())
    }
}