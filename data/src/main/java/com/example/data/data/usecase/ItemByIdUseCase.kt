package com.example.data.data.usecase

import com.example.domain.entity.Item
import com.example.data.data.repository.IItemRepository
import kotlinx.coroutines.delay

class ItemByIdUseCase(private val repository: IItemRepository):
    IUseCase<Int, Item?> {
    override suspend fun execute(data: Int): Item? {
        delay(1000)
        return repository.getItem(data)
    }
}