package com.example.foodplanner.ui.auth

import com.example.foodplanner.data.repository.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthPresenter(
    private var view: AuthContract.View?,
    private val userPrefs: UserPreferences,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthContract.Presenter {

    override fun login(email: String, pass: String) {
        if (pass.length > 20) { view?.showError("Password must not exceed 20 characters"); return }
        view?.showLoading()
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                view?.hideLoading()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userPrefs.saveUser(user?.uid ?: "", user?.email, isGuest = false)
                    view?.onSuccess()
                } else {
                    view?.showError(task.exception?.localizedMessage ?: "Login failed")
                }
            }
    }

    override fun register(username: String, email: String, pass: String) {
        if (pass.length > 20) { view?.showError("Password must not exceed 20 characters"); return }
        if (pass.length < 6) { view?.showError("Password must be at least 6 characters"); return }
        view?.showLoading()
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                view?.hideLoading()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userPrefs.saveUser(user?.uid ?: "", username, isGuest = false)
                    view?.onSuccess()
                } else {
                    view?.showError(task.exception?.localizedMessage ?: "Registration failed")
                }
            }
    }

    override fun loginAsGuest() {
        userPrefs.saveUser("guest_user", "Guest", isGuest = true)
        view?.onSuccess()
    }

    override fun start() {}
    override fun stop() { view = null }
}