package com.example.androidcomposestudyapp.di

import com.example.androidcomposestudyapp.item.vm.ItemViewModel
import com.example.androidcomposestudyapp.main.vm.MainViewModel
import com.example.data.repository.ItemRepository
import com.example.domain.repository.IItemRepository
import com.example.domain.usecase.ItemByIdUseCase
import com.example.domain.usecase.ItemsUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IItemRepository> { ItemRepository() }
    single { ItemsUseCase(get()) }
    single { ItemByIdUseCase(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ItemViewModel(get(), get()) }
}