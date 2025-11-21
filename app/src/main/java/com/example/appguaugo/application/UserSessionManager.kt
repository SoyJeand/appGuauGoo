package com.example.appguaugo.application

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Define los roles posibles en tu aplicación
enum class UserRole {
    CLIENTE,
    PASEADOR
}

// Usamos un 'object' para crear un Singleton: una única instancia para toda la app.
object UserSessionManager {

    // 1. Un StateFlow privado para guardar el rol actual.
    // Inicia por defecto en modo CLIENTE.
    private val _currentRole = MutableStateFlow(UserRole.CLIENTE)

    // 2. Un StateFlow público y de solo lectura para que la UI lo observe.
    val currentRole = _currentRole.asStateFlow()

    // 3. Una función para cambiar el rol.
    // Esto notificará automáticamente a todos los observadores.
    fun switchRole() {
        _currentRole.value = if (_currentRole.value == UserRole.CLIENTE) {
            UserRole.PASEADOR
        } else {
            UserRole.CLIENTE
        }
        // En un futuro, podrías guardar esta preferencia en SharedPreferences
        // para que la app recuerde el último rol usado al reiniciarse.
    }

    // Función para obtener el rol actual sin ser un Flow
    fun getRole(): UserRole = _currentRole.value
}
