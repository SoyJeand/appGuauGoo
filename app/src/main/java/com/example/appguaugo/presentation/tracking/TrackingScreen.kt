package com.example.appguaugo.presentation.tracking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TrackingScreen(onWalkFinished: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Placeholder para el mapa con la ruta en tiempo real
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Aquí va el mapa siguiendo el paseo")
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Paseo en Progreso", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Paseador: Juan Pérez")
                Text("Tiempo restante: 15 minutos")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onWalkFinished,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Paseo (Temporal)") // Este botón lo controlaría el paseador
                }
            }
        }
    }
}