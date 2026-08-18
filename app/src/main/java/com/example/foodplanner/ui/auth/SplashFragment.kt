package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.google.firebase.auth.FirebaseAuth
import com.example.foodplanner.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

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

        val currentUser = FirebaseAuth.getInstance().currentUser

        // Display splash for 2s before navigating based on auth state
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isAdded) return@postDelayed

            val nextFragment = if (currentUser!=null) {
                HomeFragment()
            } else {
                LoginFragment()
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, nextFragment)
                .commit()
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}