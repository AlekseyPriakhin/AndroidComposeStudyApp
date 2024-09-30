package com.example.domain.usecase

interface IUseCase<D,R> {
    suspend fun execute(data: D): R
}