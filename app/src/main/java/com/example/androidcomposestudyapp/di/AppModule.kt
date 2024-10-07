package com.example.androidcomposestudyapp.di

import com.example.androidcomposestudyapp.main.vm.MainViewModel
import com.example.data.repository.UserRepository
import com.example.domain.repository.IUserRepository
import com.example.domain.usecase.UserUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IUserRepository> { UserRepository() }
    single { UserUseCase(get()) }
    viewModel { MainViewModel(get()) }
}