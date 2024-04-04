package com.example.mviexample.data

import com.example.mviexample.model.UserUI

sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()
    data class Users(val user: List<UserUI>) : MainState()
    data class Error(val error: String?) : MainState()
}