package com.example.androidcomposestudyapp.main.vm

import com.example.domain.entity.User

sealed class MainState {
    object Loading : MainState()
    data class Error(
        val message: String
    ) : MainState()

    data class Content(
        val list: List<User>
    ) : MainState()
}