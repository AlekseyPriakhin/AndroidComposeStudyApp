package com.example.androidcomposestudyapp.di

import android.content.Context
import com.example.androidcomposestudyapp.item.vm.ItemViewModel
import com.example.androidcomposestudyapp.main.vm.MainViewModel
import com.example.data.data.repository.ItemRepository
import com.example.data.data.repository.IItemRepository
import com.example.data.data.storage.ILocalStorage
import com.example.data.data.storage.LocalStorage
import com.example.data.data.usecase.ItemByIdUseCase
import com.example.data.data.usecase.ItemsUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        get<Context>().getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
    single<IItemRepository> { ItemRepository() }
    single { ItemsUseCase(get()) }
    single { ItemByIdUseCase(get()) }
    single<ILocalStorage> { LocalStorage(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ItemViewModel(get(), get(), get()) }
}