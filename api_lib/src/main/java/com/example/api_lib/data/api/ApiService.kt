package com.example.api_lib.data.api

import com.example.api_lib.data.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>
}