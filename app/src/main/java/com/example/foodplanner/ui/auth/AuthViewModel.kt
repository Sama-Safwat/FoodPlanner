package com.example.foodplanner.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodplanner.App
import com.example.foodplanner.data.repository.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userPrefs: UserPreferences,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun login(email: String, pass: String) {
        if (pass.length > 20) {
            _error.value = "Password must not exceed 20 characters"
            return
        }
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userPrefs.saveUser(user?.uid ?: "", user?.email, isGuest = false)
                    App.instance.appScope.launch { App.instance.syncManager.restore() }
                    _isLoggedIn.value = true
                } else {
                    _error.value = task.exception?.localizedMessage ?: "Login failed"
                }
            }
    }

    fun register(username: String, email: String, pass: String) {
        if (pass.length > 20) {
            _error.value = "Password must not exceed 20 characters"
            return
        }
        if (pass.length < 6) {
            _error.value = "Password must be at least 6 characters"
            return
        }
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userPrefs.saveUser(user?.uid ?: "", username, isGuest = false)
                    App.instance.appScope.launch { App.instance.syncManager.restore() }
                    _isLoggedIn.value = true
                } else {
                    _error.value = task.exception?.localizedMessage ?: "Registration failed"
                }
            }
    }

    fun loginAsGuest() {
        userPrefs.saveUser("guest_user", "Guest", isGuest = true)
        _isLoggedIn.value = true
    }

    fun checkIfLoggedIn(): Boolean {
        return userPrefs.isLoggedIn()
    }

    fun onErrorShown() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
    }
}