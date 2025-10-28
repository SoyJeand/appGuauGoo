package com.example.appguaugo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.database.AppDatabase
import com.example.appguaugo.data.preferences.UserPreferences
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ClienteRepository
    private val prefs = UserPreferences(application)

    init {
        val clienteDao = AppDatabase.getDatabase(application).clienteDao()
        repository = ClienteRepository(clienteDao)
    }

    private val _nombreCliente = MutableStateFlow<String?>(null)
    val nombreCliente = _nombreCliente.asStateFlow()

    fun setNombreCliente(nombre: String?) {
        _nombreCliente.value = nombre
    }

    fun cargarNombreCliente(correo: String) {
        viewModelScope.launch {
            val cliente = withContext(Dispatchers.IO) {
                repository.getClientePorCorreo(correo)
            }
            _nombreCliente.value = cliente?.nombres
        }
    }

    fun guardarCorreoSesion(correo: String) {
        viewModelScope.launch {
            prefs.saveUserEmail(correo)
        }
    }

    suspend fun obtenerCorreoSesion(): String? {
        return withContext(Dispatchers.IO) {
            prefs.getUserEmailSync()
        }
    }


    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.clearUserEmail()
            _nombreCliente.value = null
        }
    }
}
