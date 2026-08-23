package com.example.foodplanner.utils

import com.google.firebase.auth.FirebaseAuth

object UserProvider {

    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUserId(): String = auth.currentUser?.uid ?: "guest"

    fun getCurrentUserEmail(): String = auth.currentUser?.email ?: ""

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun logout() = auth.signOut()
}