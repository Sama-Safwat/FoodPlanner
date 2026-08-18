package com.example.foodplanner.data.api

import com.example.foodplanner.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

object RetrofitInstance {
    private const val BASE_URL = Constants.BASE_URL
    val api: MealApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                RxJava3CallAdapterFactory.create()
            )
            .build()
            .create(MealApi::class.java)
    }
}