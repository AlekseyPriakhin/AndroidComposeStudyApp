package com.example.myapplication.details.vm

import com.example.domain.entity.Item

sealed class ItemState(val title: String) {
    data object Loading : ItemState("Loading...")
    data class Error(
        val errorTitle: String,
        val message: String
    ) : ItemState(errorTitle)

    data class Content(
        val element: Item?,
    ) : ItemState(element?.name ?: "Not found")
}