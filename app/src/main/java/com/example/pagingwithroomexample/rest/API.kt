package com.example.pagingwithroomexample.rest

import com.example.pagingwithroomexample.security.SecurityConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {

    companion object{
        const val BASE_URL = SecurityConstants.BASE_URL
        const val API_KEY = SecurityConstants.API_KEY
    }

    private lateinit var imagesApi: ImagesApi

    fun getUsersApi(): ImagesApi {

        if (!::imagesApi.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            imagesApi = retrofit.create(ImagesApi::class.java)
        }


        return imagesApi;
    }



}