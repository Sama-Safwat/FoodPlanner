package com.example.foodplanner.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodplanner.R
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Area
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.databinding.FragmentCountriesBinding
import com.example.foodplanner.ui.details.MealDetailsFragment

class CountriesFragment : Fragment(), CountriesContract.View {

    private var _binding: FragmentCountriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: CountriesContract.Presenter
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
        setupListeners()

        val repository = MealRemoteRepository(RetrofitInstance.api)
        presenter = CountriesPresenter(this, repository)
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        if (isShowingMeals) {
            binding.btnBackToCountries.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerViews() {
        countriesAdapter = CountriesAdapter { country ->
            presenter.loadMealsByCountry(country.strArea ?: "")
        }
        binding.countriesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = countriesAdapter
        }

        mealsAdapter = CountryMealsAdapter { mealId ->
            presenter.onMealClicked(mealId)
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
        presenter.loadCountries()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showCountries(countries: List<Area>) {
        countriesAdapter.submitList(countries)
        binding.countriesRecyclerView.visibility = View.VISIBLE
        binding.mealsRecyclerView.visibility = View.GONE
        binding.countryTitle.visibility = View.GONE
        binding.btnBackToCountries.visibility = View.GONE
        isShowingMeals = false
    }

    override fun showCountryMeals(meals: List<Meal>) {
        mealsAdapter.submitList(meals)
        binding.countriesRecyclerView.visibility = View.GONE
        binding.mealsRecyclerView.visibility = View.VISIBLE
        binding.countryTitle.visibility = View.VISIBLE
        binding.countryTitle.text = "Meals from this country"
        binding.btnBackToCountries.visibility = View.VISIBLE
        isShowingMeals = true
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    override fun navigateToMealDetails(mealId: String) {
        val fragment = MealDetailsFragment.newInstance(mealId)
        parentFragmentManager.beginTransaction()
            .hide(this)
            .add(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        presenter.stop()
        _binding = null
        super.onDestroyView()
    }

     override fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}