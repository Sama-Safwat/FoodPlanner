package com.example.foodplanner.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.foodplanner.data.api.RetrofitInstance
import com.example.foodplanner.data.local.AppDatabase
import com.example.foodplanner.data.model.Meal
import com.example.foodplanner.data.repository.MealRemoteRepository
import com.example.foodplanner.data.repository.MealRepository
import com.example.foodplanner.databinding.FragmentMealDetailsBinding
import com.example.foodplanner.ui.planner.PlannerActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MealDetailsFragment : Fragment(), MealDetailsContract.View {

    private var _binding: FragmentMealDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: MealDetailsContract.Presenter
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private var mealId: String = ""
    private var currentMeal: Meal? = null

    companion object {
        private const val ARG_MEAL_ID = "meal_id"

        fun newInstance(mealId: String): MealDetailsFragment {
            val fragment = MealDetailsFragment()
            val args = Bundle()
            args.putString(ARG_MEAL_ID, mealId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mealId = arguments?.getString(ARG_MEAL_ID) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setupRecyclerView()
            setupListeners()
            val remoteRepository = MealRemoteRepository(RetrofitInstance.api)
            val database = AppDatabase.getDatabase(requireContext())
            val localRepository = MealRepository(RetrofitInstance.api, database.mealDao())
            presenter = MealDetailsPresenter(this, remoteRepository, localRepository)
            if (mealId.isNotEmpty()) {
                presenter.loadMealDetails(mealId)
            } else {
                showError("No meal ID provided")
            }
        }

    private fun setupRecyclerView() {
        ingredientsAdapter = IngredientsAdapter()
        binding.ingredientsRecyclerView.apply {
            adapter = ingredientsAdapter
        }
    }

    private fun setupListeners() {
        binding.favoriteButton.setOnClickListener {
            presenter.toggleFavorite()
        }

        binding.backButton.setOnClickListener {
            presenter.onBackPressed()
        }

        binding.addToPlanButton.setOnClickListener{
            currentMeal?.let{ meal ->
                val intent = Intent(requireContext(), PlannerActivity::class.java)
                intent.putExtra("meal_id", meal.idMeal)
                intent.putExtra("meal_name", meal.strMeal)
                intent.putExtra("meal_image", meal.strMealThumb)
                startActivity(intent)
            } ?: run{
                showError("No meal to add to plan!")
            }
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showMealDetails(meal: Meal) {
        currentMeal = meal
        binding.contentLayout.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE

        binding.mealName.text = meal.strMeal ?: "Unknown Meal"
        binding.mealCountry.text = "${meal.strArea ?: "Unknown"}"
        binding.mealCategory.text = "${meal.strCategory ?: "Uncategorized"}"
        binding.instructionsText.text = meal.strInstructions ?: "No instructions available"

        Glide.with(this)
            .load(meal.strMealThumb)
            .into(binding.mealImage)
    }

    override fun showFavoriteStatus(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteButton.text = "Remove from Favorites"
            binding.favoriteButton.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
        } else {
            binding.favoriteButton.text = "Add to Favorites"
            binding.favoriteButton.setBackgroundColor(resources.getColor(android.R.color.darker_gray, null))
        }
    }

    override fun showVideo(videoUrl: String) {
        binding.videoContainer.visibility = View.VISIBLE

        val videoId = extractYouTubeId(videoUrl)
        if (videoId != null) {
            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        } else {
            binding.videoContainer.visibility = View.GONE
        }
    }

    override fun showIngredients(ingredients: List<Pair<String, String>>) {
        if (ingredients.isEmpty()) {
            binding.ingredientsSection.visibility = View.GONE
            return
        }
        binding.ingredientsSection.visibility = View.VISIBLE
        ingredientsAdapter.submitList(ingredients)
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.contentLayout.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    override fun navigateBack() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun extractYouTubeId(url: String): String? {
        val patterns = listOf(
            "v=([a-zA-Z0-9_-]{11})",
            "youtu.be/([a-zA-Z0-9_-]{11})",
            "embed/([a-zA-Z0-9_-]{11})"
        )

        for (pattern in patterns) {
            val regex = Regex(pattern)
            val matchResult = regex.find(url)
            if (matchResult != null) {
                return matchResult.groupValues[1]
            }
        }
        return null
    }

    override fun onDestroyView() {
        presenter.stop()
        binding.youtubePlayerView.release()
        _binding = null
        super.onDestroyView()
    }
}