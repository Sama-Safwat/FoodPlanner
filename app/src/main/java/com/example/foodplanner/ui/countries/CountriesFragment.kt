package com.example.foodplanner.ui.countries

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
import com.example.foodplanner.data.model.Area
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentCountriesBinding
import com.example.foodplanner.ui.details.MealDetailsFragment

class CountriesFragment : Fragment() {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CountriesViewModel
    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var mealsAdapter: CountryMealsAdapter
    private var isShowingMeals = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountriesBinding.inflate(inflater, container, false)
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
        val factory = CountriesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CountriesViewModel::class.java)

        viewModel.countries.observe(viewLifecycleOwner) { countries ->
            binding.errorText.visibility = View.GONE

            if (countries.isNotEmpty()) {
                countriesAdapter.submitList(countries)
                binding.countriesRecyclerView.visibility = View.VISIBLE
                binding.mealsRecyclerView.visibility = View.GONE
                binding.countryTitle.visibility = View.GONE
                binding.btnBackToCountries.visibility = View.GONE
                isShowingMeals = false
            }
        }

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            binding.errorText.visibility = View.GONE

            if (meals.isNotEmpty()) {
                mealsAdapter.submitList(meals)
                binding.countriesRecyclerView.visibility = View.GONE
                binding.mealsRecyclerView.visibility = View.VISIBLE
                binding.countryTitle.visibility = View.VISIBLE
                binding.countryTitle.text = "Meals from this country"
                binding.btnBackToCountries.visibility = View.VISIBLE
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
        countriesAdapter = CountriesAdapter { country ->
            viewModel.loadMealsByCountry(country.strArea ?: "")
        }
        binding.countriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = countriesAdapter
        }

        mealsAdapter = CountryMealsAdapter { mealId ->
            viewModel.onMealClicked(mealId)
        }
        binding.mealsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mealsAdapter
        }
    }

    private fun setupListeners() {
        binding.btnBackToCountries.setOnClickListener {
            showCountriesGrid()
        }
    }

    private fun showCountriesGrid() {
        isShowingMeals = false
        binding.countriesRecyclerView.visibility = View.VISIBLE
        binding.mealsRecyclerView.visibility = View.GONE
        binding.countryTitle.visibility = View.GONE
        binding.btnBackToCountries.visibility = View.GONE
        viewModel.loadCountries()
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
            binding.btnBackToCountries.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}