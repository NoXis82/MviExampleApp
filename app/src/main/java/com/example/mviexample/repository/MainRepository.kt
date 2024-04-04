package com.example.mviexample.repository

import com.example.api_lib.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getUsersApi() = apiHelper.getUsers()
}