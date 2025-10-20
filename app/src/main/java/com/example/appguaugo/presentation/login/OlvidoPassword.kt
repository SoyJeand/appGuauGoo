package com.example.appguaugo.presentation.login

import androidx.compose.runtime.getValue

import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.R // Asegúrate de que esta sea la ruta correcta a tu R
import com.example.appguaugo.ui.theme.AppGuauGoTheme // O el nombre de tu tema
import com.example.appguaugo.ui.theme.GuauBlueText

// Define los colores aquí para reutilizarlos y que sea fácil cambiarlos
val GuauYellow = Color(0xFFFBC02D) // Un amarillo dorado similar a la imagen
val GuauYellowDark = Color(0xFFF9A825) // Un poco más oscuro para el botón

@Composable
fun OlvidoPasswordScreen(
    onSendLinkClick: (email: String) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    // Estado para guardar el correo electrónico que el usuario ingresa
    var email by remember { mutableStateOf("") }

    // Estructura principal con el fondo amarillo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuauYellow)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SECCIÓN SUPERIOR AMARILLA ---
            Spacer(modifier = Modifier.height(60.dp))

            // Logo (reutilizando el estilo de las otras pantallas)
            Image(
                painter = painterResource(id = R.drawable.logo_princ_guaoguao), // ¡Usa tu logo aquí!
                contentDescription = "Logo de GuauGuao",
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título de la pantalla
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TARJETA BLANCA INFERIOR ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), // Ocupa el resto del espacio
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texto de instrucciones
                    Text(
                        text = "Ingrese el correo electrónico asociado a su cuenta y le enviaremos un enlace para que puedas restablecer su contraseña.",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        lineHeight = 24.sp // Mejora la legibilidad del párrafo
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo de texto para el correo
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo Electrónico") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón para enviar el enlace
                    Button(
                        onClick = { onSendLinkClick(email) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GuauYellowDark)
                    ) {
                        Text(
                            "ENVIAR ENLACE DE RECUPERACIÓN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Empujamos el enlace de "Volver" hacia abajo
                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de texto para volver al inicio de sesión
                    TextButton(onClick = { onBackToLoginClick() }) {
                        Text(
                            "Volver al inicio de sesión",
                            color = GuauBlueText,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Un poco de espacio al final
                }
            }
        }
    }
}

// --- PREVISUALIZACIÓN ---
@Preview(showBackground = true, device = "id:pixel_4a")
@Composable
fun ForgotPasswordScreenPreview() {
    AppGuauGoTheme {
        OlvidoPasswordScreen(
            onSendLinkClick = {},
            onBackToLoginClick = {}
        )
    }
}

