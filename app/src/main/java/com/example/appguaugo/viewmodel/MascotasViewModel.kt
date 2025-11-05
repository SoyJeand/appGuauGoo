package com.example.appguaugo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.entity.MascotaEntity
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MascotasViewModel(
    private val repository: ClienteRepository,
    private val duenoId: Int
) : ViewModel() {

    // StateFlow para exponer la lista de mascotas.
    // Se inicializa con una lista vacía y se actualiza desde el Flow del DAO.
    val mascotasState: StateFlow<List<MascotaEntity>> = repository.getMascotasByDuenoId(duenoId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // El Flow se mantiene activo 5s después de que la UI deja de observar
            initialValue = emptyList()
        )

    // Función para añadir una nueva mascota
    fun addMascota(
        nombre: String,
        tipo: String,
        raza: String,
        edad: Int,
        peso: Double,
        comportamiento: String,
        onResult: (Boolean, String) -> Unit // Callback para notificar el resultado a la UI
    ) {
        viewModelScope.launch {
            try {
                val nuevaMascota = MascotaEntity(
                    duenoId = duenoId,
                    nombre = nombre,
                    tipo = tipo,
                    raza = raza,
                    edad = edad,
                    peso = peso,
                    comportamiento = comportamiento
                )
                repository.insertMascota(nuevaMascota)
                onResult(true, "Mascota añadida con éxito")
            } catch (e: Exception) {
                onResult(false, "Error al añadir la mascota: ${e.message}")
            }
        }
    }
}
