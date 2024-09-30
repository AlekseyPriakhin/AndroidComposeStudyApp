package com.example.domain.usecase

import com.example.domain.entity.User
import com.example.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class UserUseCase(private val repository: IUserRepository): IUseCase<Unit, List<User>> {
    override suspend fun execute(data: Unit): List<User> = withContext(Dispatchers.Default) {
        delay(500)
        return@withContext repository.getUsers()
    }
}