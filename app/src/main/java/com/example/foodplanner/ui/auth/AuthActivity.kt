package com.example.foodplanner.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodplanner.MainActivity
import com.example.foodplanner.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Already logged in? Skip login screen
        if (auth.currentUser != null) {
            startMain()
            return
        }

        binding.btnLogin.setOnClickListener { submit(isRegister = false) }
        binding.btnRegister.setOnClickListener { submit(isRegister = true) }
    }

    private fun submit(isRegister: Boolean) {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email"
            return
        }
        if (password.length < 6) {
            binding.etPassword.error = "Minimum 6 characters"
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val task = if (isRegister)
            auth.createUserWithEmailAndPassword(email, password)
        else
            auth.signInWithEmailAndPassword(email, password)

        task.addOnCompleteListener { result ->
            binding.progressBar.visibility = View.GONE
            if (result.isSuccessful) {
                toast(if (isRegister) "Account created ✔" else "Welcome back ✔")
                startMain()
            } else {
                toast(result.exception?.message ?: "Authentication failed")
            }
        }
    }

    private fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


}