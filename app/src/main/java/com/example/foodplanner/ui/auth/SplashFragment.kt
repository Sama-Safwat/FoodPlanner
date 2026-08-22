package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodplanner.R
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.databinding.FragmentSplashBinding
import com.example.foodplanner.ui.home.HomeFragment

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private var hasNavigated = false
    private lateinit var viewModel: AuthViewModel

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

        val userPrefs = UserPreferences(requireContext())
        val factory = AuthViewModelFactory(userPrefs)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.lottieSplash.enableMergePathsForKitKatAndAbove(true)
        binding.lottieSplash.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2500)
    }

    private fun navigateToNextScreen() {
        if (hasNavigated || !isAdded) return
        hasNavigated = true

        val targetFragment: Fragment = if (viewModel.checkIfLoggedIn()) {
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