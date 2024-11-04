package com.example.data.data.usecase

import com.example.domain.entity.Item
import com.example.data.data.repository.IItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ItemsUseCase(private val repository: IItemRepository):
    IUseCase<Unit, List<Item>> {
    override suspend fun execute(data: Unit): List<Item> = withContext(Dispatchers.Default) {
        delay(500)
        return@withContext repository.getItems()
    }
}