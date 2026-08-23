package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodplanner.R
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.databinding.FragmentLoginBinding
import com.example.foodplanner.ui.home.HomeFragment
import com.example.foodplanner.App

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        val userPrefs = UserPreferences(requireContext())
        val factory = AuthViewModelFactory(userPrefs)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        observeViewModel()

        binding.tvRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            if (email.isEmpty() || pass.isEmpty()) {
                showError("Fill all fields")
                return@setOnClickListener
            }
            viewModel.login(email, pass)
        }

        binding.btnGuest.setOnClickListener {
            viewModel.loginAsGuest()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
                viewModel.onErrorShown()
            }
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn == true) {
                onSuccess()
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun onSuccess() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}