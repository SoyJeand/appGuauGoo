package com.example.appguaugo.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.viewmodel.ProfileUiState
import com.example.appguaugo.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar perfil")
                        }
                    } else {
                        IconButton(onClick = {
                            isEditing = false
                            // Recargar datos originales al cancelar
                            viewModel.loadUserProfile()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar edición")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (isEditing) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            viewModel.saveProfile()
                            isEditing = false
                        }
                    }
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Guardar cambios")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopStart
        ) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileUiState.Success -> {
                    EditableProfileForm(
                        user = state.user,
                        isEditing = isEditing,
                        onUserUpdate = { updatedUser ->
                            viewModel.updateUserData(updatedUser)
                        }
                    )
                }
                is ProfileUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadUserProfile() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditableProfileForm(
    user: ClienteEntity,
    isEditing: Boolean,
    onUserUpdate: (ClienteEntity) -> Unit
) {
    var nombres by remember { mutableStateOf(user.nombres) }
    var apellidos by remember { mutableStateOf(user.apellidos) }
    var telefono by remember { mutableStateOf(user.telefono ?: "") }
    var direccion by remember { mutableStateOf(user.direccion ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (isEditing) {
            EditableProfileField(
                label = "Nombres",
                value = nombres,
                onValueChange = { nombres = it },
                isEditing = isEditing
            )
            EditableProfileField(
                label = "Apellidos",
                value = apellidos,
                onValueChange = { apellidos = it },
                isEditing = isEditing
            )
            EditableProfileField(
                label = "Teléfono",
                value = telefono,
                onValueChange = { telefono = it },
                isEditing = isEditing,
                keyboardType = KeyboardType.Phone
            )
            EditableProfileField(
                label = "Dirección",
                value = direccion,
                onValueChange = { direccion = it },
                isEditing = isEditing
            )

            // Campos no editables (solo lectura)
            ProfileDetailRow("Correo Electrónico:", user.correo)
            ProfileDetailRow("Fecha de Nacimiento:", formatDate(user.fecNacimiento))
        } else {
            // Vista de solo lectura
            ProfileDetailRow("Nombre Completo:", "${user.nombres} ${user.apellidos}")
            ProfileDetailRow("Correo Electrónico:", user.correo)
            ProfileDetailRow("Teléfono:", user.telefono ?: "No especificado")
            ProfileDetailRow("Fecha de Nacimiento:", formatDate(user.fecNacimiento))
            ProfileDetailRow("Dirección:", user.direccion ?: "No especificada")
        }
    }

    // Actualizar cuando cambien los valores
    LaunchedEffect(nombres, apellidos, telefono, direccion) {
        if (isEditing) {
            val updatedUser = user.copy(
                nombres = nombres,
                apellidos = apellidos,
                telefono = telefono.ifBlank { null },
                direccion = direccion.ifBlank { null }
            )
            onUserUpdate(updatedUser)
        }
    }
}

@Composable
fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
        } else {
            Text(
                text = value.ifBlank { "No especificado" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

fun formatDate(date: Date?): String {
    if (date == null) return "No especificada"
    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return dateFormat.format(date)
}