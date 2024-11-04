package com.example.androidcomposestudyapp.item.vm

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.androidcomposestudyapp.item.ItemScreenRoute
import com.example.data.data.storage.ILocalStorage
import com.example.data.data.usecase.ItemByIdUseCase
import com.example.domain.entity.Item
import com.example.myapplication.details.vm.ItemState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel(private val useCase: ItemByIdUseCase,
                    private val savedStateHandle: SavedStateHandle,
                    private  val storage: ILocalStorage): ViewModel() {
    private val _state = MutableStateFlow<ItemState>(ItemState.Loading)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            val title = when (val currentState = _state.value) {
                is ItemState.Content -> {
                    currentState.title
                }

                is ItemState.Error -> {
                    currentState.title
                }

                ItemState.Loading -> {
                    "Ошибка"
                }
            }
            _state.emit(ItemState.Error(title, throwable.message ?: "Ошибочка"))
        }
    }

    val state: StateFlow<ItemState>
        get() = _state

    init {
        loadContent()
    }

    fun markAsRead() {
        val route = savedStateHandle.toRoute<ItemScreenRoute>()
        storage.markAsRead(route.id.toInt())
        Log.println(Log.INFO, "ItemScreen", "Read ${route.id}")
        loadContent()
    }

    fun toggleLike(element: Item?, onToggle: () -> Unit = {}) {
        if (element == null) return
        Log.println(Log.INFO, "ItemScreen", "Toggle liked ${element.id}")
        storage.toggleLike(element.id)
        onToggle()
    }

    fun isRead(): Boolean {
        val route = savedStateHandle.toRoute<ItemScreenRoute>()
        return storage.isRead(route.id.toInt())
    }

    fun isLiked(element: Item?): Boolean {
        if(element == null) return false;
        return storage.isLiked(element.id)
    }

    private fun loadContent() {
        viewModelScope.launch(context = exceptionHandler) {
            val route = savedStateHandle.toRoute<ItemScreenRoute>()
            val result = useCase.execute(route.id.toInt())
            _state.emit(ItemState.Content(result))
        }
    }
}