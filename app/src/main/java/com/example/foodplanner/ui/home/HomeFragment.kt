package com.example.foodplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentHomeBinding
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.example.foodplanner.ui.search.SearchFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var mealsAdapter: HomeMealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        val repository = MealRemoteRepository(RetrofitInstance.api)
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        viewModel.mealOfTheDay.observe(viewLifecycleOwner) { meal ->
            meal?.let { showMeal(it) }
        }

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            if (meals.isNotEmpty()) {
                mealsAdapter.submitList(meals)
                binding.errorText.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingText.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = it
                binding.loadingText.visibility = View.GONE
            }
        }
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
        binding.searchCard.setOnClickListener { openSearch() }
        binding.tvSearch.setOnClickListener { openSearch() }
        binding.tvSeeAll.setOnClickListener { openSearch() }

        binding.mealCard.setOnClickListener {
            viewModel.mealOfTheDay.value?.idMeal?.let { mealId ->
                navigateToMealDetails(mealId)
            }
        }
    }

    private fun openSearch() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SearchFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun showMeal(meal: Meal) {
        binding.mealCard.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        binding.loadingText.visibility = View.GONE

        binding.mealName.text = meal.strMeal ?: "Unknown meal"
        binding.mealCountry.text = "Country: ${meal.strArea ?: "Unknown"}"

        Glide.with(this)
            .load(meal.strMealThumb)
            .centerCrop()
            .into(binding.mealImage)
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
        _binding = null
        super.onDestroyView()
    }
}