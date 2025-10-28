package com.example.appguaugo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.preferences.PaseoPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class Paseo(
    val clienteEmail: String,
    val paseadorNombre: String,
    val direccion: String,
    val fechaHora: String
)

class PaseoViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PaseoPreferences(application)

    private val _paseos = MutableStateFlow<List<Paseo>>(emptyList())
    val paseos: StateFlow<List<Paseo>> = _paseos

    init {
        viewModelScope.launch {
            prefs.paseosGuardados.collect { lista ->
                val paseos = lista.mapNotNull { json ->
                    val parts = json.split("|")
                    if (parts.size == 4)
                        Paseo(parts[0], parts[1], parts[2], parts[3])
                    else null
                }
                _paseos.value = paseos
            }
        }
    }

    fun crearPaseo(email: String, paseador: String, direccion: String) {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val nuevo = Paseo(email, paseador, direccion, fecha)

        viewModelScope.launch {
            val cadena = listOf(nuevo.clienteEmail, nuevo.paseadorNombre, nuevo.direccion, nuevo.fechaHora).joinToString("|")
            prefs.guardarPaseo(cadena)
        }
    }

    fun limpiarHistorial() {
        viewModelScope.launch {
            prefs.limpiarPaseos()
        }
    }
}
