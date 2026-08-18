package com.example.foodplanner.ui.auth

import com.example.foodplanner.data.repository.UserPreferences
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthPresenter(
    private var view: AuthContract.View?,
    private val userPrefs: UserPreferences
) : AuthContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun start() {}

    override fun stop() {
        disposables.clear()
        view = null
    }

    override fun login(email: String, pass: String) {
        if (pass.length > 20) {
            view?.showError("Password must not exceed 20 characters")
            return
        }

        view?.showLoading()
        val disposable = Single.just("mock_user_id_" + System.currentTimeMillis())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userId ->
                    userPrefs.saveUser(userId, isGuest = false)
                    view?.hideLoading()
                    view?.onSuccess()
                },
                { error ->
                    view?.hideLoading()
                    view?.showError(error.localizedMessage ?: "Login failed")
                }
            )
        disposables.add(disposable)
    }

    override fun register(username: String, email: String, pass: String) {
        if (pass.length > 20) {
            view?.showError("Password must not exceed 20 characters")
            return
        }
        if (pass.length < 6) {
            view?.showError("Password must be at least 6 characters")
            return
        }

        view?.showLoading()

        val disposable = Single.just(Pair("mock_user_id", username))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { (userId, name) ->
                    userPrefs.saveUser(userId, name, isGuest = false)
                    view?.hideLoading()
                    view?.onSuccess()
                },
                { error ->
                    view?.hideLoading()
                    view?.showError(error.localizedMessage ?: "Registration failed")
                }
            )
        disposables.add(disposable)
    }

    override fun loginAsGuest() {
        userPrefs.saveUser("guest_user", "Guest", isGuest = true)
        view?.onSuccess()
    }
}