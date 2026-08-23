package com.example.foodplanner.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentSearchBinding
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.google.android.material.tabs.TabLayout

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private var currentSearchType = SearchType.NAME

    enum class SearchType {
        NAME, CATEGORY, INGREDIENT, COUNTRY
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
        setupViewModel()
    }

    private fun setupViewModel() {
        val repository = MealRemoteRepository(RetrofitInstance.api)
        val factory = SearchViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        viewModel.searchResults.observe(viewLifecycleOwner) { meals ->
            if (meals.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.errorContainer.visibility = View.GONE
                binding.emptyText.visibility = View.GONE
                binding.resultsRecyclerView.visibility = View.VISIBLE
                searchResultsAdapter.submitMeals(meals)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == true) {
                binding.progressBar.visibility = View.VISIBLE
                binding.errorContainer.visibility = View.GONE
                binding.resultsRecyclerView.visibility = View.GONE
                binding.emptyText.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.progressBar.visibility = View.GONE
                binding.resultsRecyclerView.visibility = View.GONE
                binding.emptyText.visibility = View.GONE
                binding.errorContainer.visibility = View.VISIBLE
                binding.errorText.text = it
                binding.retryButton.visibility = View.VISIBLE
                viewModel.onErrorShown()
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
        }

        viewModel.ingredients.observe(viewLifecycleOwner) { ingredients ->
        }

        viewModel.navigateToMealDetails.observe(viewLifecycleOwner) { mealId ->
            mealId?.let {
                navigateToMealDetails(it)
                viewModel.onNavigationDone()
            }
        }
    }

    private fun setupTabs() {
        val tabTitles = listOf("Name", "Category", "Ingredient", "Country")

        binding.tabLayout.removeAllTabs()
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentSearchType = when (tab?.position) {
                    0 -> SearchType.NAME
                    1 -> SearchType.CATEGORY
                    2 -> SearchType.INGREDIENT
                    else -> SearchType.COUNTRY
                }
                binding.searchInput.hint = when (currentSearchType) {
                    SearchType.NAME -> "Search by meal name..."
                    SearchType.CATEGORY -> "Search by category..."
                    SearchType.INGREDIENT -> "Search by ingredient..."
                    SearchType.COUNTRY -> "Search by Country..."
                }
                clearResults()
                binding.searchInput.text?.clear()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.tabLayout.getTabAt(0)?.select()
        binding.searchInput.hint = "Search by meal name..."
    }

    private fun setupSearchListener() {
        binding.searchInputLayout.setEndIconOnClickListener {
            performSearch()
        }
        binding.retryButton.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        val query = binding.searchInput.text.toString().trim()
        if (query.isEmpty()) {
            binding.progressBar.visibility = View.GONE
            binding.resultsRecyclerView.visibility = View.GONE
            binding.emptyText.visibility = View.GONE
            binding.errorContainer.visibility = View.VISIBLE
            binding.errorText.text = "Please enter a search term"
            binding.retryButton.visibility = View.VISIBLE
            return
        }
        when (currentSearchType) {
            SearchType.NAME -> viewModel.searchByName(query)
            SearchType.CATEGORY -> viewModel.searchByCategory(query)
            SearchType.INGREDIENT -> viewModel.searchByIngredient(query)
            SearchType.COUNTRY -> viewModel.searchByCountry(query)
        }
    }

    private fun setupRecyclerView() {
        searchResultsAdapter = SearchResultsAdapter(
            onMealClick = { mealId ->
                viewModel.onMealClicked(mealId)
            },
            onIngredientClick = { ingredient ->
                binding.searchInput.setText(ingredient)
                viewModel.searchByIngredient(ingredient)
            }
        )
        binding.resultsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchResultsAdapter
        }
    }

    private fun clearResults() {
        searchResultsAdapter.clear()
        binding.resultsRecyclerView.visibility = View.GONE
        binding.emptyText.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
    }

    private fun navigateToMealDetails(mealId: String) {
        val fragment = MealDetailsFragment.newInstance(mealId)
        parentFragmentManager.beginTransaction()
            .hide(this)
            .add(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}