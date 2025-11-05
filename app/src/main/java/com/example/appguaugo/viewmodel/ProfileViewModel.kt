package com.example.appguaugo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Estados posibles de la UI del Perfil
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: ClienteEntity) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val repository: ClienteRepository,
    private val userId: Int // Recibe el ID del usuario a mostrar
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            repository.getClienteById(userId)
                .catch { e ->
                    // Si ocurre un error en el Flow
                    _uiState.value = ProfileUiState.Error("Error al cargar el perfil: ${e.message}")
                }
                .collect { user ->
                    // Cuando el Flow emite un valor
                    if (user != null) {
                        _uiState.value = ProfileUiState.Success(user)
                    } else {
                        _uiState.value = ProfileUiState.Error("Usuario no encontrado.")
                    }
                }
        }
    }
}

