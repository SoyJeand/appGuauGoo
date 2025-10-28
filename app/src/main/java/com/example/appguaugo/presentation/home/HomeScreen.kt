package com.example.appguaugo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appguaugo.viewmodel.MainViewModel
import com.example.appguaugo.viewmodel.PaseoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    paseoViewModel: PaseoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val paseos by paseoViewModel.paseos.collectAsState()
    val nombre by mainViewModel.nombreCliente.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Solicitar un Paseo") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 🗺️ Mapa simulado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Aquí va el mapa con paseadores cercanos")
            }

            // 🐾 Contenido inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hola ${nombre ?: "usuario"} 👋",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("¿Dónde recogemos a tu mascota?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                var direccion by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Introduce tu dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val correo = mainViewModel.nombreCliente.value ?: "Desconocido"
                        paseoViewModel.crearPaseo(correo, "Juan el Paseador", direccion)
                        direccion = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar Paseador")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Historial de paseos:",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    items(paseos) { paseo ->
                        Text(
                            text = "📍 ${paseo.direccion} — ${paseo.fechaHora}",
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { paseoViewModel.limpiarHistorial() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Limpiar historial", color = Color.White)
                }
            }
        }
    }
}
