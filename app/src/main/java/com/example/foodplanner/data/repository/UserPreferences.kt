package com.example.foodplanner.data.repository

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun getUserId(): String? = prefs.getString("USER_ID", null)
    fun getUsername(): String? = prefs.getString("USERNAME", null)
    fun isGuest(): Boolean = prefs.getBoolean("IS_GUEST", false)

    fun saveUser(userId: String, username: String? = null, isGuest: Boolean) {
        prefs.edit()
            .putString("USER_ID", userId)
            .putString("USERNAME", username)
            .putBoolean("IS_GUEST", isGuest)
            .putBoolean("IS_LOGGED_IN", true)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("IS_LOGGED_IN", false)&& !isGuest()

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}