package com.example.data.data.storage

interface ILocalStorage {
    fun markAsRead(id: Int)
    fun isRead(id: Int): Boolean

    fun toggleLike(id: Int)
    fun isLiked(id: Int): Boolean
}