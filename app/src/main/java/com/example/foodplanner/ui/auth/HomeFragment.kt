package com.example.foodplanner.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temporary placeholder feedback
        Toast.makeText(context, "Welcome to Home (Placeholder)", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()

// Navigate back to LoginFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }


}