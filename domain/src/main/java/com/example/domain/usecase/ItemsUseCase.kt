package com.example.domain.usecase

import com.example.domain.entity.Item
import com.example.domain.repository.IItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ItemsUseCase(private val repository: IItemRepository): IUseCase<Unit, List<Item>> {
    override suspend fun execute(data: Unit): List<Item> = withContext(Dispatchers.Default) {
        delay(500)
        return@withContext repository.getItems()
    }
}