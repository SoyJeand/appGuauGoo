package com.example.appguaugo.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.appguaugo.ui.theme.GuauYellow
import com.example.appguaugo.ui.theme.GuauYellowDark
import com.example.appguaugo.viewmodel.RegisterUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class) // Necesario para el DatePicker
@Composable
fun RegisterScreen(
    registerState: RegisterUiState,
    onRegisterClick: (
        nombres: String,
        apellidos: String,
        correo: String,
        contrasenha: String,
        fecNacimiento: String?,
        direccion: String?,
        telefono: String?
    ) -> Unit,
    onGoToLoginClick: () -> Unit
) {
    // Estados para cada campo de texto
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasenha by remember { mutableStateOf("") }
    var confirmContrasenha by remember { mutableStateOf("") }
    // 1. Estado para el OBJETO Date. Lo usaremos internamente en la pantalla.
    var fecNacimiento by remember { mutableStateOf<Date?>(null) }

    val fecNacimientoString = remember(fecNacimiento) {
        if(fecNacimiento == null) "" else SimpleDateFormat("dd/MM/yyyy",
            Locale.ROOT).format(fecNacimiento)
    }
    // 3. Estado para controlar la visibilidad del diálogo del DatePicker.
    var mostrarDatePicker by remember { mutableStateOf(false) }


    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }


    // Estados para la visibilidad de las contraseñas
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // Estructura principal con fondo amarillo
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
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_princ_guaoguao), // ¡USA TU LOGO AQUÍ!
                contentDescription = "Logo de GuauGuao",
                modifier = Modifier
                    .size(80.dp) // Un poco más pequeño que en el login, como en la imagen
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Crea tu cuenta",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- TARJETA BLANCA INFERIOR ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp) // El padding se aplica aquí
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()), // ¡IMPORTANTE PARA EL SCROLL!
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- CAMPOS DE TEXTO ---
                        CustomOutlinedTextField(
                            value = nombres,
                            onValueChange = { nombres = it },
                            label = "Nombre(s)"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomOutlinedTextField(
                            value = apellidos,
                            onValueChange = { apellidos = it },
                            label = "Apellido(s)"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomOutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = "Correo Electrónico",
                            keyboardType = KeyboardType.Email
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PasswordTextField(
                            value = contrasenha,
                            onValueChange = { contrasenha = it },
                            label = "Contraseña",
                            isVisible = isPasswordVisible,
                            onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PasswordTextField(
                            value = confirmContrasenha,
                            onValueChange = { confirmContrasenha = it },
                            label = "Confirmar Contraseña",
                            isVisible = isConfirmPasswordVisible,
                            onVisibilityChange = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // --- MODIFICADO: Reemplazamos el TextField de fecha por uno interactivo ---
                        OutlinedTextField(
                            value = fecNacimientoString, // Muestra el String formateado.
                            onValueChange = { /* No se puede cambiar escribiendo */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Log.d("RegisterScreen", "Click detectado en el campo de fecha") // <-- AÑADE ESTE LOG
                                    mostrarDatePicker = true }, // Al hacer clic, abre el diálogo.
                            label = { Text("Fecha de Nacimiento") },
                            shape = RoundedCornerShape(12.dp),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                // Puedes ajustar también el color del ícono si lo tuvieras
                                // disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                // disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )// Es importante para que no aparezca el teclado.
                            )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomOutlinedTextField(
                            value = direccion,
                            onValueChange = { direccion = it },
                            label = "Dirección"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomOutlinedTextField(
                            value = telefono,
                            onValueChange = { telefono = it },
                            label = "Telefono"
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // --- BOTÓN DE REGISTRO ---
                        Button(
                            onClick = {
                                onRegisterClick(
                                    nombres,
                                    apellidos,
                                    correo,
                                    contrasenha,
                                    if (fecNacimientoString.isBlank()) null else fecNacimientoString, // Pasamos el string o null
                                    direccion,
                                    telefono
                                )
                            },
                            enabled = registerState !is RegisterUiState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GuauYellowDark)
                        ) {
                            if (registerState is RegisterUiState.Loading) {
                                // Si el estado es "Loading", mostramos un indicador de carga.
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White, // Color del spinner
                                    strokeWidth = 3.dp // Grosor del spinner
                                )
                            } else {
                                // Si no está cargando, mostramos el texto normal.
                                Text("REGISTRAR CUENTA", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp)) // Espacio final
                    }

                }
                // Hacemos que la columna sea scrollable para que quepa en pantallas pequeñas

            }
        }
    }
    // --- MODIFICADO: Añadimos la lógica del DatePickerDialog al final del Composable ---
    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fecNacimiento?.time
        )
        val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Actualizamos nuestro estado con el objeto Date seleccionado
                            fecNacimiento = Date(millis)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// --- Componentes reutilizables para limpiar el código ---

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onVisibilityChange: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña"
            IconButton(onClick = onVisibilityChange) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

/*
// --- PREVISUALIZACIÓN ---
@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun RegisterScreenPreview() {
    AppGuauGoTheme {
        RegisterrScreen(onRegisterClick = { _, _, _, _, _, _, _, -> })
    }
}*/
