package com.example.appguaugo.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var notificacionesOn by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Configuración") })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                ListItem(
                    headlineContent = { Text("Notificaciones") },
                    trailingContent = {
                        Switch(
                            checked = notificacionesOn,
                            onCheckedChange = { notificacionesOn = it }
                        )
                    }
                )
            }
            item { Divider() }
            item {
                ListItem(
                    headlineContent = { Text("Cambiar Contraseña") },
                    modifier = Modifier.clickable { /* TODO: Navegar a pantalla de cambio de contraseña */ }
                )
            }
            item { Divider() }
            item {
                ListItem(
                    headlineContent = { Text("Términos y Condiciones") },
                    modifier = Modifier.clickable { /* TODO: Navegar a pantalla de términos */ }
                )
            }
            item { Divider() }
            item {
                ListItem(
                    headlineContent = { Text("Cerrar Sesión") },
                    modifier = Modifier.clickable { /* TODO: Lógica para cerrar sesión */ }
                )
            }
        }
    }
}
