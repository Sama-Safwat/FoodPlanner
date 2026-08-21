package com.example.foodplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentHomeBinding
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.example.foodplanner.ui.search.SearchFragment

class HomeFragment : Fragment(), MealContract.View {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: MealContract.Presenter
    private lateinit var mealsAdapter: HomeMealAdapter

    private var currentMealId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()

        val repository = MealRemoteRepository(
            RetrofitInstance.api
        )

        presenter = MealPresenter(
            view = this,
            repository = repository
        )

        presenter.start()
    }

    private fun setupRecyclerView() {

        mealsAdapter = HomeMealAdapter { meal ->

            meal.idMeal?.let { mealId ->
                navigateToMealDetails(mealId)
            }
        }

        binding.recyclerMeals.apply {

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = mealsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {

        binding.searchCard.setOnClickListener {
            openSearch()
        }

        binding.tvSearch.setOnClickListener {
            openSearch()
        }

        binding.mealCard.setOnClickListener {

            currentMealId?.let { mealId ->
                navigateToMealDetails(mealId)
            }
        }

        binding.tvSeeAll.setOnClickListener {
            openSearch()
        }
    }

    private fun openSearch() {

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                SearchFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    override fun showLoading() {

        binding.loadingText.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
    }

    override fun hideLoading() {

        binding.loadingText.visibility = View.GONE
    }

    override fun showMeal(meal: Meal) {

        binding.mealCard.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        binding.loadingText.visibility = View.GONE

        currentMealId = meal.idMeal

        binding.mealName.text =
            meal.strMeal ?: "Unknown meal"

        binding.mealCountry.text =
            "Country: ${meal.strArea ?: "Unknown"}"

        Glide.with(this)
            .load(meal.strMealThumb)
            .centerCrop()
            .into(binding.mealImage)
    }

    override fun showMeals(meals: List<Meal>) {

        if (meals.isNotEmpty()) {
            binding.errorText.visibility = View.GONE
            mealsAdapter.submitList(meals)
        }
    }

    override fun showError(message: String) {

        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    private fun navigateToMealDetails(mealId: String) {

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                MealDetailsFragment.newInstance(mealId)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {

        presenter.stop()

        _binding = null

        super.onDestroyView()
    }
}