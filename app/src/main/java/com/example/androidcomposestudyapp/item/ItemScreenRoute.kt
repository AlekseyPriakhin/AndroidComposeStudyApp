package com.example.androidcomposestudyapp.item

import com.example.androidcomposestudyapp.navigation.IRoute
import kotlinx.serialization.Serializable

@Serializable
data class ItemScreenRoute(
    val id: Long
) : IRoute