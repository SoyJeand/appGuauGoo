package com.example.appguaugo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Estados posibles de la UI del Perfil
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: ClienteEntity) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val repository: ClienteRepository,
    private val userId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var currentUser: ClienteEntity? = null

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            repository.getClienteById(userId)
                .catch { e ->
                    _uiState.value = ProfileUiState.Error("Error al cargar el perfil: ${e.message}")
                }
                .collect { user ->
                    if (user != null) {
                        currentUser = user
                        _uiState.value = ProfileUiState.Success(user)
                    } else {
                        _uiState.value = ProfileUiState.Error("Usuario no encontrado.")
                    }
                }
        }
    }

    fun updateUserData(updatedUser: ClienteEntity) {
        currentUser = updatedUser
        // Actualizamos el estado para reflejar los cambios en tiempo real
        _uiState.value = ProfileUiState.Success(updatedUser)
    }

    fun saveProfile() {
        viewModelScope.launch {
            val userToSave = currentUser ?: return@launch
            try {
                _uiState.value = ProfileUiState.Loading
                val rowsAffected = withContext(Dispatchers.IO) {
                    repository.updateCliente(userToSave)
                }

                if (rowsAffected > 0) {
                    _uiState.value = ProfileUiState.Success(userToSave)
                    // En una app real, podrías mostrar un Toast de éxito aquí
                } else {
                    _uiState.value = ProfileUiState.Error("No se pudieron guardar los cambios")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al guardar: ${e.message}")
            }
        }
    }
}

