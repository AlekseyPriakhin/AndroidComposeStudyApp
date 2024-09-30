package com.example.data.repository

import com.example.domain.entity.User
import com.example.domain.repository.IUserRepository
import kotlinx.coroutines.delay

class UserRepository: IUserRepository {
    override suspend fun getUsers(): List<User> {
        delay(5_000);
        //"https://networklessons.com/wp-content/uploads/2013/02/stub-tree.jpg"
        return listOf(User(1, "Alex", null, null ),
            User(2, "Alice", null, "blabla"))
    }
}