package com.example.data.data.repository

import com.example.domain.entity.Item

interface IItemRepository {
    suspend fun getItems(): List<Item>
    suspend fun getItem(id: Int): Item?
}