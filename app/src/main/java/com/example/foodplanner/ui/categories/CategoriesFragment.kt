package com.example.foodplanner.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Category
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(), CategoriesContract.View {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: CategoriesContract.Presenter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var mealsAdapter: CategoryMealsAdapter

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

        val repository = MealRemoteRepository(RetrofitInstance.api)
        presenter = CategoriesPresenter(this, repository)
        presenter.start()
    }

    private fun setupRecyclerViews() {
        categoriesAdapter = CategoriesAdapter { category ->
            presenter.loadMealsByCategory(category.strCategory ?: "")
        }
        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = categoriesAdapter
        }

        mealsAdapter = CategoryMealsAdapter { mealId ->
            presenter.onMealClicked(mealId)
        }
        binding.mealsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mealsAdapter
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showCategories(categories: List<Category>) {
        categoriesAdapter.submitList(categories)
        binding.categoriesRecyclerView.visibility = View.VISIBLE
        binding.mealsRecyclerView.visibility = View.GONE
        binding.categoryTitle.visibility = View.GONE
    }

    override fun showCategoryMeals(meals: List<Meal>) {
        mealsAdapter.submitList(meals)
        binding.mealsRecyclerView.visibility = View.VISIBLE
        binding.categoryTitle.visibility = View.VISIBLE
        binding.categoryTitle.text = "Meals in this category"
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    override fun navigateToMealDetails(mealId: String) {

    }

    override fun onDestroyView() {
        presenter.stop()
        _binding = null
        super.onDestroyView()
    }
}