package com.example.androidcomposestudyapp.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import com.example.domain.usecase.UserUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val useCase = UserUseCase(UserRepository())

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch {
            _state.emit(MainState.Error(throwable.message ?: "Ошибочка"))
        }
    }

    val state: StateFlow<MainState>
        get() = _state


    init {
        viewModelScope.launch(context = exceptionHandler) {
            val result = useCase.execute(Unit)
            _state.emit(MainState.Content(result))
        }
    }
}