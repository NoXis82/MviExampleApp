package com.example.api_lib.data.repository

import com.example.api_lib.data.api.ApiHelper
import com.example.api_lib.data.api.ApiService
import com.example.api_lib.data.model.User

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }

}