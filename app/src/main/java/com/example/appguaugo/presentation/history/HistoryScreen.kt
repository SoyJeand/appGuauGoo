package com.example.appguaugo.presentation.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Data class de ejemplo para un paseo
data class Paseo(val id: Int, val fecha: String, val paseador: String, val calificacion: Int)

val listaDePaseos = listOf(
    Paseo(1, "12 de Octubre, 2025", "Juan Pérez", 5),
    Paseo(2, "10 de Octubre, 2025", "María López", 4),
    Paseo(3, "05 de Octubre, 2025", "Juan Pérez", 5)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Historial de Paseos") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.padding(8.dp)
        ) {
            items(listaDePaseos) { paseo ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    ListItem(
                        headlineContent = { Text("Paseador: ${paseo.paseador}") },
                        supportingContent = { Text("Fecha: ${paseo.fecha}") },
                        trailingContent = { Text("⭐ ${paseo.calificacion}") }
                    )
                }
            }
        }
    }
}