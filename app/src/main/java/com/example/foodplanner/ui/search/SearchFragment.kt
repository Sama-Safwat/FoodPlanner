package com.example.foodplanner.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentSearchBinding
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment(), SearchContract.View {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: SearchContract.Presenter
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private var currentSearchType = SearchType.NAME

    enum class SearchType {
        NAME, CATEGORY, INGREDIENT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabs()
        setupSearchListener()

        val repository = MealRemoteRepository(RetrofitInstance.api)
        presenter = SearchPresenter(this, repository)
        presenter.start()
    }

    private fun setupTabs() {
        val tabTitles = listOf("Name", "Category", "Ingredient")

        binding.tabLayout.removeAllTabs()
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                currentSearchType = when (tab?.position) {
                    0 -> SearchType.NAME
                    1 -> SearchType.CATEGORY
                    else -> SearchType.INGREDIENT
                }
                binding.searchInput.hint = when (currentSearchType) {
                    SearchType.NAME -> "Search by meal name..."
                    SearchType.CATEGORY -> "Search by category..."
                    SearchType.INGREDIENT -> "Search by ingredient..."
                }
                clearResults()
                binding.searchInput.text?.clear()
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

        binding.tabLayout.getTabAt(0)?.select()
        binding.searchInput.hint = "Search by meal name..."
    }

    private fun setupSearchListener() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchInput.text.toString().trim()
            when (currentSearchType) {
                SearchType.NAME -> presenter.searchByName(query)
                SearchType.CATEGORY -> presenter.searchByCategory(query)
                SearchType.INGREDIENT -> presenter.searchByIngredient(query)
            }
        }

        binding.retryButton.setOnClickListener {
            val query = binding.searchInput.text.toString().trim()
            when (currentSearchType) {
                SearchType.NAME -> presenter.searchByName(query)
                SearchType.CATEGORY -> presenter.searchByCategory(query)
                SearchType.INGREDIENT -> presenter.searchByIngredient(query)
            }
        }
    }

    private fun setupRecyclerView() {
        searchResultsAdapter = SearchResultsAdapter(onMealClick =  { mealId ->
            presenter.onMealClicked(mealId)
        }, onIngredientClick = { ingredient ->
            binding.searchInput.setText(ingredient)
            presenter.searchByIngredient(ingredient)
        }
        )
        binding.resultsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchResultsAdapter
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
        binding.resultsRecyclerView.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showSearchResults(meals: List<Meal>) {
        binding.progressBar.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.resultsRecyclerView.visibility = View.VISIBLE
        searchResultsAdapter.submitMeals(meals)
    }

    override fun showCategories(categories: List<String>) {
    }

    override fun showIngredients(ingredients: List<String>) {
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.resultsRecyclerView.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.errorText.text = message
        binding.retryButton.visibility = View.VISIBLE
    }

    override fun clearResults() {
        searchResultsAdapter.clear()
        binding.resultsRecyclerView.visibility = View.GONE
        binding.emptyText.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
    }

    override fun navigateToMealDetails(mealId: String) {
        val fragment = MealDetailsFragment.newInstance(mealId)
        parentFragmentManager.beginTransaction()
            .hide(this)
            .add(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        presenter.stop()
        _binding = null
        super.onDestroyView()
    }
}