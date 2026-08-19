package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.databinding.FragmentLoginBinding
import com.example.foodplanner.ui.home.HomeFragment

class LoginFragment : Fragment(R.layout.fragment_login), AuthContract.View {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: AuthContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<View>(R.id.navContainer)?.visibility = View.GONE
        _binding = FragmentLoginBinding.bind(view)
        presenter = AuthPresenter(this, UserPreferences(requireContext()))

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
            presenter.login(email, pass)
        }

        binding.btnGuest.setOnClickListener {
            presenter.loginAsGuest()
        }
    }

    override fun showLoading() { binding.progressBar.visibility = View.VISIBLE }
    override fun hideLoading() { binding.progressBar.visibility = View.GONE }
    override fun showError(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    override fun onSuccess() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}