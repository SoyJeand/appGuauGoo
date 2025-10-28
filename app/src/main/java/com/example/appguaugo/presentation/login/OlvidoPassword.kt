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
import com.example.appguaugo.R
import com.example.appguaugo.ui.theme.AppGuauGoTheme
import com.example.appguaugo.ui.theme.GuauBlueText

// Colores reutilizables
val GuauYellow = Color(0xFFFBC02D)
val GuauYellowDark = Color(0xFFF9A825)

@Composable
fun OlvidoPasswordScreen(
    onSendLinkClick: (email: String) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    // Estados
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    // Validación del formato de correo
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    val isEmailValid = email.matches(emailRegex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuauYellow)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_princ_guaoguao),
                contentDescription = "Logo de GuauGuao",
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
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
                    Text(
                        text = "Ingrese el correo electrónico asociado a su cuenta y le enviaremos un enlace para que puedas restablecer su contraseña.",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo de texto del correo
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo Electrónico") },
                        singleLine = true,
                        isError = emailError,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    // Mensaje de error
                    if (emailError) {
                        Text(
                            text = "Por favor ingrese un correo válido",
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón con validación y desactivación automática
                    Button(
                        onClick = {
                            if (isEmailValid) {
                                onSendLinkClick(email)
                            } else {
                                emailError = true
                            }
                        },
                        enabled = email.isNotBlank() && isEmailValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEmailValid) GuauYellowDark else Color.Gray
                        )
                    ) {
                        Text(
                            "ENVIAR ENLACE DE RECUPERACIÓN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de texto para volver al login
                    TextButton(onClick = { onBackToLoginClick() }) {
                        Text(
                            "Volver al inicio de sesión",
                            color = GuauBlueText,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

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
