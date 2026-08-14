package com.example.foodplanner.base

interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun showError(message: String)
}