package com.example.appguaugo.viewmodel

import androidx.lifecycle.*
import com.example.appguaugo.data.entity.SolicitudPaseoEntity
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.launch

class RequestWalkViewModel(private val repository: ClienteRepository) : ViewModel() {

    fun guardarSolicitud(
        clienteId: Int,
        origen: String,
        destino: String,
        mascotaNombre: String,
        tipoPaseo: String,
        observaciones: String,
        costoOfrecido: Double,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val nuevaSolicitud = SolicitudPaseoEntity(
                    clienteId = clienteId,
                    origen = origen,
                    destino = destino,
                    mascotaNombre = mascotaNombre,
                    tipoPaseo = tipoPaseo,
                    observaciones = observaciones,
                    costoOfrecido = costoOfrecido
                )
                repository.insertSolicitudPaseo(nuevaSolicitud)
                onResult(true, "Solicitud guardada con éxito")
            } catch (e: Exception) {
                onResult(false, "Error al guardar: ${e.message}")
            }
        }
    }
}

class RequestWalkViewModelFactory(private val repository: ClienteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestWalkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RequestWalkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
