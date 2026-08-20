package com.example.foodplanner.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.databinding.FragmentHomeBinding
import com.example.foodplanner.ui.auth.LoginFragment
import com.example.foodplanner.ui.details.MealDetailsFragment
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), MealContract.View {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentMealId: String? = null
    private lateinit var presenter: MealContract.Presenter

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
        activity?.findViewById<View>(R.id.navContainer)?.visibility = View.VISIBLE
        binding.btnLogout.setOnClickListener {
            logout()
        }
        setupListeners()
        val repository = MealRemoteRepository(RetrofitInstance.api)

        presenter = MealPresenter(
            view = this,
            repository = repository
        )

        presenter.start()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.mealCard.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showMeal(meal: Meal) {
        binding.mealCard.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        currentMealId = meal.idMeal
        binding.mealName.text = meal.strMeal ?: "Unknown meal"
        binding.mealCountry.text = "Country: ${meal.strArea ?: "Unknown"}"

        Glide.with(this)
            .load(meal.strMealThumb)
            .into(binding.mealImage)
    }

    override fun showError(message: String) {
        binding.mealCard.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    override fun onDestroyView() {
        presenter.stop()
        _binding = null
        super.onDestroyView()
    }

    private fun setupListeners(){
        binding.mealCard.setOnClickListener {
            currentMealId?.let { mealId ->
                navigateToMealDetails(mealId)
            } ?: run {
                showError("No meal selected!")
            }
        }
    }

    private fun navigateToMealDetails(mealId: String){
        try {
            val bundle = Bundle().apply {
                putString("meal_id", mealId)
            }
            val fragment = MealDetailsFragment.newInstance(mealId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }catch (e: Exception){
            showError("Error opening meal details: ${e.message}")
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        UserPreferences(requireContext()).clearSession()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginFragment())
            .commit()
    }
}