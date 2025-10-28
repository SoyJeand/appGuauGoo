package com.example.appguaugo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.database.AppDatabase
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val cliente: ClienteEntity) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ClienteRepository

    init {
        val clienteDao = AppDatabase.getDatabase(application).clienteDao()
        repository = ClienteRepository(clienteDao)
    }

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(correo: String, contrasenha: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            try {
                val cliente = repository.validarCliente(correo, contrasenha)
                if (cliente != null) {
                    _loginState.value = LoginUiState.Success(cliente)
                } else {
                    _loginState.value = LoginUiState.Error("Correo o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Error al conectar con la base de datos")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }
}
