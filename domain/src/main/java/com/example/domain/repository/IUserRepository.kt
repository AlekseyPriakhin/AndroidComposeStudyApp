package com.example.domain.repository

import com.example.domain.entity.User

interface IUserRepository {
    suspend fun getUsers(): List<User>
}