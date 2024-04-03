package com.example.api_lib.data.api

import com.example.api_lib.data.model.User

interface ApiHelper {
    suspend fun getUsers(): List<User>
}