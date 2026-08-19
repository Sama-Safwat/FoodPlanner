package com.example.foodplanner.utils

import androidx.fragment.app.Fragment
import com.example.foodplanner.R
import com.example.foodplanner.data.repository.UserPreferences
import com.example.foodplanner.ui.auth.LoginFragment
import com.google.android.material.snackbar.Snackbar

object AuthGuard {

    fun requireLogin(
        fragment: Fragment,
        userPrefs: UserPreferences,
        action: () -> Unit
    ) {
        if (userPrefs.isGuest()) {
            val root = fragment.view ?: return
            Snackbar.make(root, "Please log in to use this feature", Snackbar.LENGTH_LONG)
                .setAction("Sign In") {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, LoginFragment())
                        .commit()
                }
                .show()
        } else {
            action()
        }
    }
}