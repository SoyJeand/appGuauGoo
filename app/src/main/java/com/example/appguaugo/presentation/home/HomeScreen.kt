package com.example.appguaugo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onSearchClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Solicitar un Paseo") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            // Placeholder para el mapa. Aquí integrarías el mapa de Google.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Aquí va el mapa con paseadores cercanos")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text("¿Dónde recogemos a tu mascota?", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Introduce tu dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSearchClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar Paseador")
                }
            }
        }
    }
}