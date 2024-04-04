package com.example.mviexample.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.api_lib.data.api.ApiHelper
import com.example.mviexample.data.MainState
import com.example.mviexample.intent.MainIntent
import com.example.mviexample.model.UserUI
import com.example.mviexample.repository.MainRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MviViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUser -> fetchUser()
                }
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Users(
                    repository.getUsersApi().map { user ->
                        UserUI(
                            id = user.id,
                            name = user.name,
                            email = user.email,
                            avatar = user.avatar
                        )
                    }
                )
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }
}

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MviViewModel::class.java)) {
            return MviViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}