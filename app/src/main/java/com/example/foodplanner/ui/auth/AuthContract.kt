package com.example.foodplanner.ui.auth

import com.example.foodplanner.base.BasePresenter
import com.example.foodplanner.base.BaseView

interface AuthContract {
    interface View : BaseView {
        fun onSuccess()
    }

    interface Presenter : BasePresenter {
        fun login(email: String, pass: String)
        fun register(username: String, email: String, pass: String)
        fun loginAsGuest()
    }
}