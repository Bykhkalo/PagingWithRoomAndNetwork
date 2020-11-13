package com.example.pagingwithroomexample.rest

import com.example.pagingwithroomexample.rest.API.Companion.API_KEY
import com.example.viewimages.model.ImagePage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("/api/")
    fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("image_type") type: String = "all",
        @Query("key") apiKey:String = API_KEY
    ): Call<ImagePage>

}