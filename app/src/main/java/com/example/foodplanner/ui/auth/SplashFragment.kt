package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.databinding.FragmentSplashBinding
import com.example.foodplanner.ui.home.HomeFragment

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private var hasNavigated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Enable Merge Paths on Lottie programmatically
        binding.lottieSplash.enableMergePathsForKitKatAndAbove(true)
        binding.lottieSplash.playAnimation()

        // 2. Safety Timeout: Force navigation after 2.5 seconds no matter what
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2500)
    }

    private fun navigateToNextScreen() {
        if (hasNavigated || !isAdded) return
        hasNavigated = true

        val userPrefs = UserPreferences(requireContext())
        val isLoggedIn = userPrefs.isLoggedIn()

        android.util.Log.d("SPLASH_DEBUG", "Navigating! Is Logged In: $isLoggedIn")
        val targetFragment: Fragment = if (userPrefs.isLoggedIn()) {
            HomeFragment()
        } else {
            LoginFragment()
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, targetFragment)
            .commitAllowingStateLoss()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}