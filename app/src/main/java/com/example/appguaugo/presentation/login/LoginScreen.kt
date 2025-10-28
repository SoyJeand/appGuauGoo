package com.example.appguaugo.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appguaugo.viewmodel.LoginViewModel
import com.example.appguaugo.viewmodel.MainViewModel
import com.example.appguaugo.viewmodel.LoginUiState

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    mainViewModel: MainViewModel
) {
    val loginState by loginViewModel.loginState.collectAsState()
    var correo by remember { mutableStateOf("") }
    var contrasenha by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = contrasenha,
                onValueChange = { contrasenha = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                loginViewModel.login(correo, contrasenha)
            }) {
                Text("Iniciar sesión")
            }

            when (val state = loginState) {
                is LoginUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                is LoginUiState.Success -> {
                    val cliente = state.cliente
                    mainViewModel.setNombreCliente(cliente.nombres)
                    mainViewModel.guardarCorreoSesion(cliente.correo)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                    loginViewModel.resetState()
                }

                is LoginUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                    loginViewModel.resetState()
                }

                else -> {}
            }
        }
    }
}
