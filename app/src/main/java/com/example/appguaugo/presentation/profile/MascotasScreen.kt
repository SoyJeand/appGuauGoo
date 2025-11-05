package com.example.appguaugo.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.data.entity.MascotaEntity
import com.example.appguaugo.viewmodel.MascotasViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotasScreen(
    viewModel: MascotasViewModel,
    onNavigateBack: () -> Unit
) {
    val mascotas by viewModel.mascotasState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Mascotas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Mascota")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (mascotas.isEmpty()) {
                Text("Aún no tienes mascotas. ¡Añade una!")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mascotas) { mascota ->
                        MascotaItem(mascota = mascota)
                    }
                }
            }
        }

        // El Bottom Sheet que contiene el formulario
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                AddMascotaForm(viewModel = viewModel) {
                    // Cierra el sheet después de añadir la mascota
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MascotaItem(mascota: MascotaEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(mascota.nombre, style = MaterialTheme.typography.titleLarge)
            Divider()
            Text("Tipo: ${mascota.tipo} - Raza: ${mascota.raza}", fontSize = 16.sp)
            Text("Edad: ${mascota.edad} años - Peso: ${mascota.peso} kg", fontSize = 16.sp)
            if (mascota.comportamiento.isNotBlank()) {
                Text("Notas: ${mascota.comportamiento}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun AddMascotaForm(viewModel: MascotasViewModel, onMascotaAdded: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var comportamiento by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .navigationBarsPadding(), // Para evitar que el teclado tape los campos
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Añadir Nueva Mascota", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.CenterHorizontally))
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo (Ej: Perro)") })
        OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") })
        OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad (años)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = comportamiento, onValueChange = { comportamiento = it }, label = { Text("Comportamiento o notas") })

        Button(
            onClick = {
                val edadInt = edad.toIntOrNull()
                val pesoDouble = peso.toDoubleOrNull()
                if (nombre.isBlank() || tipo.isBlank() || raza.isBlank() || edadInt == null || pesoDouble == null) {
                    Toast.makeText(context, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.addMascota(nombre, tipo, raza, edadInt, pesoDouble, comportamiento) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        onMascotaAdded()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Mascota")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
