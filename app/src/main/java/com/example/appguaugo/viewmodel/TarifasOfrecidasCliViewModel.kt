package com.example.appguaugo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appguaugo.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Modelo de datos para la UI (no es una entity de Room)
data class OfertaPaseador(
    val id: Int,
    val nombre: String,
    val fotoResId: Int,
    val precioOfrecido: Double
)

class TarifasOfrecidasViewModel : ViewModel() {
    private val _ofertas = MutableStateFlow<List<OfertaPaseador>>(emptyList())
    val ofertas: StateFlow<List<OfertaPaseador>> = _ofertas

    init {
        cargarOfertasSimuladas()
    }

    private fun cargarOfertasSimuladas() {
        _ofertas.value = listOf(
            OfertaPaseador(1, "Carlos Pérez", R.drawable.logo_princ_guaoguao, 23.50),
            OfertaPaseador(2, "Ana Gómez", R.drawable.logo_princ_guaoguao, 25.00),
            OfertaPaseador(3, "Luis Fernández", R.drawable.logo_princ_guaoguao, 22.00)
        )
    }

    // En el futuro, aquí podrías tener funciones para aceptar/rechazar ofertas
}
