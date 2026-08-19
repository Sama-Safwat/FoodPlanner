package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.databinding.FragmentRegisterBinding
import com.example.foodplanner.ui.home.HomeFragment

class RegisterFragment : Fragment(R.layout.fragment_register), AuthContract.View {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: AuthContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)

        presenter = AuthPresenter(this, UserPreferences(requireContext()))

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("Fill all fields")
                return@setOnClickListener
            }

            presenter.register(username, email, password)
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess() {
        Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
    }

    override fun onDestroyView() {
        presenter.stop()
        _binding = null
        super.onDestroyView()
    }
}