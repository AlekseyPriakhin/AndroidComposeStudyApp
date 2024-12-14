package com.example.data.data.repository

import com.example.domain.entity.Item
import kotlinx.coroutines.delay

class ItemRepository: IItemRepository {
    private val items = listOf(Item(1, "Item1", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRQfr1rXTMG-svUWWtNQvvas4TJ6LrEFTBP5dhwAl3hIJ9GCmM0SapCQ_pxR-bIUicblifb", null ),
        Item(2, "Item2", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRQfr1rXTMG-svUWWtNQvvas4TJ6LrEFTBP5dhwAl3hIJ9GCmM0SapCQ_pxR-bIUicblifb", "blabla"),
        Item(3,
            "Item3",
            "https://networklessons.com/wp-content/uploads/2013/02/stub-tree.jpg",
            "Описание"))


    override suspend fun getItems(): List<Item> {
        delay(5_000)
        //"https://networklessons.com/wp-content/uploads/2013/02/stub-tree.jpg"
        return items
    }

    override suspend fun getItem(id: Int): Item? {
        return items.find { u -> u.id == id }
    }
}