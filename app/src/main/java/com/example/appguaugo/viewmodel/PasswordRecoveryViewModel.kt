package com.example.appguaugo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

sealed class PasswordRecoveryState {
    object Idle : PasswordRecoveryState()
    object Loading : PasswordRecoveryState()
    object Success : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
}

class PasswordRecoveryViewModel(private val repository: ClienteRepository) : ViewModel() {

    private val _recoveryState = MutableStateFlow<PasswordRecoveryState>(PasswordRecoveryState.Idle)
    val recoveryState = _recoveryState.asStateFlow()

    fun sendRecoveryEmail(email: String) {
        viewModelScope.launch {
            _recoveryState.value = PasswordRecoveryState.Loading
            try {
                // Verificar si el correo existe en la base de datos
                val clienteExists = withContext(Dispatchers.IO) {
                    repository.checkEmailExists(email)
                }

                if (clienteExists) {
                    // Generar token de recuperación
                    val recoveryToken = generateRecoveryToken()

                    // Simular envío de email (en una app real, aquí integrarías con tu servicio de email)
                    sendRecoveryEmailToUser(email, recoveryToken)

                    _recoveryState.value = PasswordRecoveryState.Success
                } else {
                    _recoveryState.value = PasswordRecoveryState.Error("El correo electrónico no está registrado en nuestro sistema")
                }
            } catch (e: Exception) {
                _recoveryState.value = PasswordRecoveryState.Error("Error: ${e.message ?: "No se pudo procesar la solicitud"}")
            }
        }
    }

    private fun generateRecoveryToken(): String {
        return UUID.randomUUID().toString().substring(0, 8).uppercase()
    }

    private suspend fun sendRecoveryEmailToUser(email: String, token: String) {
        // Simular tiempo de envío de email
        delay(2000)

        // En una app real, aquí integrarías con:
        // - Firebase Auth sendPasswordResetEmail()
        // - SendGrid, Amazon SES, o tu propio servidor SMTP
        // - Guardar el token en la base de datos para validación posterior

        println("SIMULACIÓN: Email de recuperación enviado a $email con token: $token")
    }

    fun resetRecoveryState() {
        _recoveryState.value = PasswordRecoveryState.Idle
    }
}