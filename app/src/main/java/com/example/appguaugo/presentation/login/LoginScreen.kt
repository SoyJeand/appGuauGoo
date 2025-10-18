package com.example.appguaugo.presentation.login // O el paquete que corresponda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.R // Asegúrate de que esta sea la ruta correcta a tu R
import com.example.appguaugo.ui.theme.GuauBlueText
import com.example.appguaugo.ui.theme.GuauYellow
import com.example.appguaugo.ui.theme.GuauYellowDark
import com.example.appguaugo.viewmodel.LoginUiState

@Composable
fun LoginScreen(
    loginState: LoginUiState,
    onLoginClick: (correo: String, contrasenha: String) -> Unit,
    onGoogleLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    // Estados para guardar el contenido de los campos de texto
    var correo by remember { mutableStateOf("") }
    var contrasenha by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Estructura principal con un Box para superponer los dos colores de fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuauYellow) // Fondo amarillo
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SECCIÓN SUPERIOR AMARILLA ---
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_guaugo), // ¡USA TU LOGO AQUÍ!
                contentDescription = "Logo de GuauGuao",
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Bienvenido de vuelta!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(30.dp))

            // --- TARJETA BLANCA INFERIOR ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), // Ocupa el resto del espacio
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- CAMPOS DE TEXTO ---
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo Electrónico") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = contrasenha,
                        onValueChange = { contrasenha = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Contraseña") },
                        singleLine = true,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = "Mostrar/Ocultar contraseña")
                            }
                        }
                    )

                    TextButton(
                        onClick = onForgotPasswordClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("¿Olvidaste tu contraseña?", color = GuauBlueText, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- BOTONES ---
                    Button(
                        onClick = { onLoginClick(correo, contrasenha) },
                        enabled = loginState !is LoginUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GuauYellowDark)
                    ) {
                        if (loginState is LoginUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text("INICIAR SESIÓN", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("O inicia sesión con", color = Color.Gray, fontSize = 14.sp)


                    OutlinedButton(
                        onClick = onGoogleLoginClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google_logo), // ¡USA TU LOGO DE GOOGLE!
                                contentDescription = "Logo de Google",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("GOOGLE", color = Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Spacer para empujar el último texto hacia abajo
                    Spacer(modifier = Modifier.height(50.dp))

                    TextButton(onClick = onRegisterClick) {
                        Text("¿No tienes cuenta? Regístrate.", color = GuauBlueText, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

/*// --- PREVISUALIZACIÓN ---
// ¡La mejor herramienta de Compose para ver tus diseños sin ejecutar la app!
@Preview(showBackground = true, device = "id:pixel_8_pro")
@Composable
fun LoginScreenPreview() {
    AppGuauGoTheme { // Usa tu tema para una previsualización más precisa
        LoginnScreen(
            loginState = { LoginUiState },
            onLoginClick = { _, _ -> },
            onGoogleLoginClick = { },
            onRegisterClick = { },
            onForgotPasswordClick = { }
        )
    }
}*/


