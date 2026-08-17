package com.example.foodplanner.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentSearchBinding
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
        val tabTitles = listOf("Search", "Category", "Ingredient")
        val adapter = SearchTabAdapter(tabTitles)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
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
        searchResultsAdapter = SearchResultsAdapter { mealId ->
            presenter.onMealClicked(mealId)
        }
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
        searchResultsAdapter.submitList(meals)
    }

    override fun showCategories(categories: List<String>) {
    }

    override fun showIngredients(ingredients: List<String>) {
        binding.progressBar.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
        binding.resultsRecyclerView.visibility = View.VISIBLE
        searchResultsAdapter.submitList(ingredients)
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
        searchResultsAdapter.submitList(emptyList())
        binding.resultsRecyclerView.visibility = View.GONE
        binding.emptyText.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
    }

    override fun navigateToMealDetails(mealId: String) {
    }

    override fun onDestroyView() {
        presenter.stop()
        _binding = null
        super.onDestroyView()
    }
}