package com.example.appguaugo.presentation.walker_home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkerHomeScreen(
    openDrawer: () -> Unit // Evento para abrir el menú lateral
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modo Paseador") },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aquí se mostrará un mapa con las solicitudes de paseo cercanas.",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
