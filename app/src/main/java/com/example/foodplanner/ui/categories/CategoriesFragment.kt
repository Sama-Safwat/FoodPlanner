package com.example.foodplanner.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentCategoriesBinding
import com.example.foodplanner.ui.details.MealDetailsFragment

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var mealsAdapter: CategoryMealsAdapter
    private var isShowingMeals = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        val repository = MealRemoteRepository(RetrofitInstance.api)
        val factory = CategoriesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CategoriesViewModel::class.java)
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            binding.errorText.visibility = View.GONE
            if (categories.isNotEmpty()) {
                categoriesAdapter.submitList(categories)
                binding.categoriesRecyclerView.visibility = View.VISIBLE
                binding.mealsRecyclerView.visibility = View.GONE
                binding.categoryTitle.visibility = View.GONE
                binding.btnBackToCategories.visibility = View.GONE
                isShowingMeals = false
            }
        }
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.errorText.visibility = View.GONE
            if (meals.isNotEmpty()) {
                mealsAdapter.submitList(meals)
                binding.categoriesRecyclerView.visibility = View.GONE
                binding.mealsRecyclerView.visibility = View.VISIBLE
                binding.categoryTitle.visibility = View.VISIBLE
                binding.categoryTitle.text = "Meals in this category"
                binding.btnBackToCategories.visibility = View.VISIBLE
                isShowingMeals = true
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = error
            } else {
                binding.errorText.visibility = View.GONE
            }
        }
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
        viewModel.navigateToMealDetails.observe(viewLifecycleOwner) { mealId ->
            mealId?.let {
                navigateToMealDetails(it)
                viewModel.onNavigationDone()
            }
        }
    }

    private fun setupRecyclerViews() {
        categoriesAdapter = CategoriesAdapter { category ->
            viewModel.loadMealsByCategory(category.strCategory ?: "")
        }
        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoriesAdapter
        }

        mealsAdapter = CategoryMealsAdapter { mealId ->
            viewModel.onMealClicked(mealId)
        }
        binding.mealsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mealsAdapter
        }
    }

    private fun setupListeners() {
        binding.btnBackToCategories.setOnClickListener {
            showCategoriesGrid()
        }
    }

    private fun showCategoriesGrid() {
        isShowingMeals = false
        binding.categoriesRecyclerView.visibility = View.VISIBLE
        binding.mealsRecyclerView.visibility = View.GONE
        binding.categoryTitle.visibility = View.GONE
        binding.btnBackToCategories.visibility = View.GONE
        viewModel.loadCategories()
    }

    private fun navigateToMealDetails(mealId: String) {
        val fragment = MealDetailsFragment.newInstance(mealId)
        parentFragmentManager.beginTransaction()
            .hide(this)
            .add(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if (isShowingMeals) {
            binding.btnBackToCategories.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}