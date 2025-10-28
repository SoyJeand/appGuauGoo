package com.example.appguaugo.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appguaugo.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val nombre by mainViewModel.nombreCliente.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Bienvenido ${nombre ?: "Usuario"}")

        Button(onClick = {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }) {
            Text(text = "Cerrar Sesión")
        }
    }
}
