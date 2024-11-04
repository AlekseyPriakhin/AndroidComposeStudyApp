package com.example.data.data.usecase

interface IUseCase<D,R> {
    suspend fun execute(data: D): R
}