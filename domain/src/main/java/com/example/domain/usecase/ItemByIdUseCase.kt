package com.example.domain.usecase

import com.example.domain.entity.Item
import com.example.domain.repository.IItemRepository
import kotlinx.coroutines.delay

class ItemByIdUseCase(private val repository: IItemRepository): IUseCase<Int, Item?> {
    override suspend fun execute(data: Int): Item? {
        delay(1000)
        return repository.getItem(data)
    }
}