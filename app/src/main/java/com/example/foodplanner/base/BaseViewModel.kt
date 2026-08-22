package com.example.foodplanner.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val errorHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable.message ?: "Unknown error")
    }

    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch(errorHandler) {
            block()
        }
    }

    abstract fun handleError(message: String)
}