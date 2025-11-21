package com.example.appguaugo.presentation.home

import androidx.activity.result.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.R
import com.example.appguaugo.ui.theme.AppGuauGoTheme
import com.example.appguaugo.ui.theme.GuauYellow
import com.example.appguaugo.ui.theme.GuauYellowDark
import kotlinx.coroutines.launch
// Importaciones para Google Maps Compose
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
// Importaciones para la ubicación y permisos
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.location.LocationManagerCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.ImagePainter
import com.example.appguaugo.application.GuauApp
import com.example.appguaugo.application.UserRole
import com.example.appguaugo.data.repository.ClienteRepository
import com.example.appguaugo.viewmodel.ProfileUiState
import com.example.appguaugo.viewmodel.ProfileViewModel
import com.example.appguaugo.viewmodel.ProfileViewModelFactory
import com.example.appguaugo.viewmodel.RequestWalkViewModel
import com.example.appguaugo.viewmodel.RequestWalkViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.CameraPositionState


// Define los colores aquí para reutilizarlos // --- DATOS DE EJEMPLO (Sin cambios) ---
data class Pet(val id: Int, val name: String, val photoResId: Int)
val samplePets = listOf(
    Pet(1, "Rocky", R.drawable.logo_princ_guaoguao),
    Pet(2, "Luna", R.drawable.logo_princ_guaoguao),
    Pet(3, "Max", R.drawable.logo_princ_guaoguao)
)
val walkTypes = listOf("Corto (30 min)", "Normal (1 h)", "Extendido (2h)")

// --- PANTALLA PRINCIPAL CON MAPA Y PANEL DESLIZABLE ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class) // <-- Añade)
@Composable
fun RequestWalkScreen(
    navController: NavController, // <<< Lo necesitamos para la navegación
    openDrawer: () -> Unit  // NUEVO PARAMETRO
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val prefs = context.getSharedPreferences("mi_app_prefs", Context.MODE_PRIVATE)
    val loggedInUserId = prefs.getInt("logged_in_user_id", -1)

    val repository = remember { ClienteRepository(
        GuauApp.db.clienteDao(),
        GuauApp.db.mascotaDao(),
        GuauApp.db.solicitudPaseoDao()) }
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(repository, loggedInUserId)
    )
    // Observamos el estado para obtener el nombre.
    val profileState by profileViewModel.uiState.collectAsState()
    val userName = when (val state = profileState) {
        is ProfileUiState.Success -> state.user.nombres // Usamos el nombre real
        else -> "Cargando..." // Texto temporal mientras se cargan los datos
    }

    val requestWalkViewModel: RequestWalkViewModel = viewModel(
        factory = RequestWalkViewModelFactory(repository) // Asume que 'repository' ya está disponible
    )

    // El "scope" se usa para abrir/cerrar el drawer mediante código

    val geocoder = remember { Geocoder(context) }

    var origin by remember { mutableStateOf("") } // el valor con el que inicializa la variable origin.
    var destination by remember { mutableStateOf("") }
    var selectedPet by remember { mutableStateOf<Pet?>(null) }
    var selectedWalkType by remember { mutableStateOf(walkTypes.first()) }
    var observations by remember { mutableStateOf("") }

    // --- ▼▼▼ CAMBIO 1: AÑADIR ESTADOS PARA EL NUMBER SHEET ▼▼▼ ---
    var showCostSheet by remember { mutableStateOf(false) }
    var costValue by remember { mutableStateOf("") }
    val costSheetState = rememberModalBottomSheetState()


    // 1. ESTADO PARA LA UBICACIÓN DEL MARCADOR
    var markerLocation by remember { mutableStateOf(LatLng(-12.0464, -77.0428)) } // Lima como inicial
    // 2. ESTADO DE LA CÁMARA
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerLocation, 12f)
    }




    // 1. Mover el estado de los permisos aquí
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    // 2. Mover el launcher para solicitar permisos aquí
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                getCurrentLocation(context) { latLng ->
                    markerLocation = latLng
                    // Mueve la cámara y actualiza el campo de texto
                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                            durationMs = 1000
                        )
                    }
                    getAddressFromLatLng(geocoder, latLng) { address -> origin = address }
                }
            }
        }
    )

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // Este bloque se ejecuta cuando el usuario vuelve de la pantalla de ajustes.
        // Aquí podríamos volver a comprobar si ya activó la ubicación y re-intentar.
        // Por simplicidad, podemos dejar que el usuario presione el botón de nuevo.
    }

    val handleMyLocationClick: () -> Unit = {
        // --- ▼▼▼ LÓGICA ACTUALIZADA ▼▼▼ ---

        // 1. Primero, verificar los PERMISOS de la aplicación
        if (locationPermissionsState.allPermissionsGranted) {

            // 2. Si hay permisos, AHORA verificar si el GPS del teléfono está encendido
            if (isLocationEnabled(context)) {
                // ¡TODO CORRECTO! El GPS está encendido, obtenemos la ubicación.
                getCurrentLocation(context) { latLng ->
                    markerLocation = latLng
                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                            durationMs = 1000
                        )
                    }
                    getAddressFromLatLng(geocoder, latLng) { address -> origin = address }
                }
            } else {
                // EL GPS ESTÁ APAGADO.
                // Le pedimos al usuario que lo encienda.
                // OPCIÓN A: Mostrar un Toast (rápido y simple)
                Toast.makeText(context, "Por favor, activa la ubicación de tu dispositivo.", Toast.LENGTH_LONG).show()

                // OPCIÓN B: Llevarlo a los ajustes (recomendado)
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                locationSettingsLauncher.launch(intent)
            }

        } else {
            // NO HAY PERMISOS. Solicitarlos como ya lo hacías.
            requestPermissionLauncher.launch(locationPermissionsState.permissions.map { it.permission }.toTypedArray())
        }
    }

    val sheetState = rememberStandardBottomSheetState (
        // Inicia el panel en un estado parcialmente expandido
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                WalkRequestForm(
                    // ... parámetros del formulario ...
                    origin = origin,
                    onOriginChange = { origin = it },
                    destination = destination,
                    onDestinationChange = { destination = it },
                    selectedPet = selectedPet,
                    onPetSelected = { selectedPet = it },
                    selectedWalkType = selectedWalkType,
                    onWalkTypeSelected = { selectedWalkType = it },
                    observations = observations,
                    onObservationsChange = { observations = it },
                    onRequestWalkClick = {
                        // Ahora, en lugar de guardar, abrimos el panel para pedir el costo.
                        // Validamos que los campos principales no estén vacíos antes de continuar.
                        if (origin.isBlank() || destination.isBlank() || selectedPet == null) {
                            Toast.makeText(context, "Completa el recorrido y selecciona una mascota.", Toast.LENGTH_LONG).show()
                        } else {
                            showCostSheet = true
                        }
                    }
                )
            },
            sheetPeekHeight = 200.dp,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetShadowElevation = 8.dp,
            sheetContainerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            // El contenido de fondo (detrás del panel)
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                MapView(
                    cameraPositionState = cameraPositionState,
                    markerLocation = markerLocation,
                    onMapLongClick = { latLng ->
                        markerLocation = latLng // Actualiza el estado del marcador
                        getAddressFromLatLng(geocoder, latLng) { address ->
                            origin = address // Actualiza el campo de texto
                        }
                    }
                ) // Tu mapa
                // 4. ICONO PARA ABRIR EL MENÚ
                IconButton(
                    onClick =  openDrawer, // CAMBIO CLAVE!!
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Abrir menú de navegación"
                    )
                }

                FloatingActionButton(
                    onClick = { handleMyLocationClick() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Lo alineamos a la esquina inferior derecha
                        .padding(
                            bottom = paddingValues.calculateBottomPadding() + 16.dp,
                            end = 16.dp
                        ), // Aplicamos el padding del panel + un margen
                    containerColor = Color.White,
                    contentColor = GuauYellowDark
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Mi Ubicación")
                }
            }
        }

        if (showCostSheet) {
            NumberSheet(
                sheetState = costSheetState,
                value = costValue,
                onValueChange = { costValue = it },
                onDismiss = {
                    showCostSheet = false
                    costValue = "" // Limpia el valor si el usuario cancela
                },
                onConfirm = {
                    showCostSheet = false
                    val costoDouble = costValue.toDoubleOrNull()

                    if (costoDouble == null || costoDouble <= 0) {
                        Toast.makeText(context, "Ingresa un costo válido.", Toast.LENGTH_SHORT).show()
                        return@NumberSheet
                    }

                    // --- 3. LLAMA AL VIEWMODEL PARA GUARDAR ---
                    requestWalkViewModel.guardarSolicitud(
                        clienteId = loggedInUserId,
                        origen = origin,
                        destino = destination,
                        mascotaNombre = selectedPet?.name ?: "Mascota no especificada",
                        tipoPaseo = selectedWalkType,
                        observaciones = observations,
                        costoOfrecido = costoDouble
                    ) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            // --- 4. NAVEGA A LA NUEVA PANTALLA ---
                            navController.navigate("tarifas_ofrecidas")
                        }
                    }
                }
            )
        }

}

// --- NUEVO COMPOSABLE: CONTENIDO DEL MENÚ LATERAL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
    userName: String,
    onProfileClick: () -> Unit,
    onMyPetsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    currentRole: UserRole,
    onSwitchRole: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    ModalDrawerSheet {
        // 1. CABECERA DEL MENÚ
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GuauYellow.copy(alpha = 0.2f))
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_princ_guaoguao), // Usa una foto de perfil real aquí
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, GuauYellow, CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = userName, // Usa el nombre real del usuario aquí
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(16.dp))


        NavigationDrawerItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Mi Perfil") },
            selected = false, // `selected` se usa para resaltar el ítem actual
            onClick = onProfileClick
        )

        // --- Ítems de Navegación del Cliente ---
        // Solo se muestran si el rol es CLIENTE
        if (currentRole == UserRole.CLIENTE) {
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Pets, contentDescription = null) },
                label = { Text("Mis Mascotas") },
                selected = false,
                onClick = onMyPetsClick,
                // badge = { Text("3") } // Opcional: para mostrar un contador
            )
        }

        if (currentRole == UserRole.PASEADOR) {
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Star, contentDescription = null) },
                label = { Text("Mis Paseos") },
                selected = false,
                onClick = { /* TODO: Navegar a la pantalla de calificaciones del paseador */ }
            )
        }

        // Divisor para separar las acciones principales del cierre de sesión
        Divider(modifier = Modifier.padding(vertical = 16.dp))

        RolSwitchButton(
            currentRole = currentRole, // Pasas el estado actual
            onClickConfirm = {
                showConfirmDialog = true
            }
        )

        // 3. ACCIÓN DE CERRAR SESIÓN
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Logout, contentDescription = null) },
            label = { Text("Cerrar Sesión") },
            selected = false,
            onClick = onLogoutClick
        )
    }


    if (showConfirmDialog) {
        AlertDialog(
            // --- Acción cuando el usuario quiere cerrar el diálogo (clic fuera, botón atrás) ---
            onDismissRequest = {
                showConfirmDialog = false
            },
            // --- Título del diálogo ---
            title = {
                Text(text = "Confirmar Cambio de Rol")
            },
            // --- Mensaje principal del diálogo ---
            text = {
                Text("¿Estás seguro de que quieres cambiar al modo ${if (currentRole == UserRole.CLIENTE) "Paseador" else "Cliente"}?")
            },
            // --- Botón de Confirmación ---
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false // Cierra el diálogo
                        onSwitchRole()        // ¡Ahora sí, ejecuta la acción de cambio!
                    }
                ) {
                    Text("Sí, cambiar")
                }
            },
            // --- Botón de Cancelación ---
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false // Simplemente cierra el diálogo
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun RolSwitchButton(
    currentRole: UserRole,
    onClickConfirm: () -> Unit // Función que se llama si el usuario CONFIRMA el cambio
) {
    // Definir el texto del botón basado en el rol actual
    val buttonText = if (currentRole == UserRole.CLIENTE) {
        "CAMBIAR A MODO PASEADOR"
    } else {
        "VOLVER A MODO CLIENTE"
    }

    // Estilo visual
    val buttonColor = Color(0xFFFDC000) // Amarillo/Naranja

    // Aquí es donde se llama a la función de CONFIRMACIÓN
    Button(
        onClick = onClickConfirm,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        ),
        // Shape hace el borde redondeado como un óvalo
        shape = RoundedCornerShape(percent = 50),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(56.dp)
    ) {
        // Solo el texto, sin el Switch
        Text(
            text = buttonText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- FORMULARIO (EXTRAÍDO PARA MAYOR CLARIDAD) ---
@Composable
fun WalkRequestForm(
    origin: String,
    destination: String,
    selectedPet: Pet?,
    selectedWalkType: String,
    observations: String,
    onOriginChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onPetSelected: (Pet) -> Unit,
    onWalkTypeSelected: (String) -> Unit,
    onObservationsChange: (String) -> Unit,
    onRequestWalkClick: () -> Unit
) {
    Column(
        modifier = Modifier
            // Altura máxima del panel para que no ocupe toda la pantalla al expandirse
            .heightIn(max = 600.dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        /*// "Asa" para indicar que el panel es deslizable
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(Color.LightGray, CircleShape)
                .align(Alignment.CenterHorizontally)
        )*/
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "Solicitar un Paseo",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(painter = painterResource(id = R.drawable.dog_walking_icon),
                contentDescription = null,
                modifier = Modifier.size(25.dp))

        }


        // --- 1. SECCIÓN DE UBICACIÓN ---
        Section(title = "Recorrido del paseo") {
            LocationInputs(
                origin = origin,
                destination = destination,
                onOriginChange = onOriginChange,
                onDestinationChange = onDestinationChange
            )
        }

        // --- 2. SECCIÓN DE MASCOTA ---
        Section(title = "Selecciona tu mascota") {
            PetSelector(
                pets = samplePets,
                selectedPet = selectedPet,
                onPetSelected = onPetSelected
            )
        }

        // --- 3. TIPO DE PASEO ---
        Section(title = "Tipo de paseo") {
            SegmentedButtons(
                options = walkTypes,
                selectedOption = selectedWalkType,
                onOptionSelected = onWalkTypeSelected
            )
        }

        // --- 4. OBSERVACIONES --- (Anteriormente 5)
        Section(title = "Instrucciones especiales") {
            OutlinedTextField(
                value = observations,
                onValueChange = onObservationsChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = { Text("Ej: Mi perro se asusta con ruidos fuertes...") },
                shape = RoundedCornerShape(12.dp)
            )
        }

        // --- 5. BOTÓN DE ACCIÓN --- (Anteriormente 6)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRequestWalkClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GuauYellowDark)
        ) {
            Text(
                "Solicitar 🐾",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

// --- SIMULADOR DE MAPA ---
// --- MAPA INTERACTIVO (REEMPLAZO DE MapSimulator) ---
@Composable
fun MapView(
    cameraPositionState: CameraPositionState,
    markerLocation: LatLng,
    onMapLongClick: (LatLng) -> Unit
    //onLocationSelected: (String) -> Unit
) {

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        // --- CAPTURA EL LONG PRESS ---
        onMapLongClick = onMapLongClick
    ) {
            // El marcador ahora se basa en el estado `markerLocation`
        Marker(
            state = MarkerState(position = markerLocation),
            title = "Ubicación seleccionada"
        )
    }

}

// --- FUNCIONES DE AYUDA (Helper Functions) ---
private fun getAddressFromLatLng(geocoder: Geocoder, latLng: LatLng, onAddressFound: (String) -> Unit) {
    try {
        // fromLocation() puede ser lento, idealmente se llamaría desde una corrutina
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        val address = addresses?.firstOrNull()
        val addressText = address?.getAddressLine(0) ?: "Dirección no encontrada"
        onAddressFound(addressText)
    } catch (e: Exception) {
        onAddressFound("Error al obtener la dirección")
    }
}

private fun getCurrentLocation(context: Context, onLocationFound: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
            if (location != null) {
                onLocationFound(LatLng(location.latitude, location.longitude))
            }
        }
            .addOnFailureListener {
                // Opcional pero recomendado: Manejar el caso en que no se pudo obtener la ubicación
                // Por ejemplo, mostrar un Toast al usuario.
                Log.e("LocationError", "No se pudo obtener la ubicación actual.", it)
                // Aquí podrías intentar con lastLocation como un último recurso
            }

    } catch (e: SecurityException) {
        Log.e("LocationPermission", "Faltan permisos de ubicación para getCurrentLocation", e)
    }
}

// --- ▼▼▼ AÑADE ESTA FUNCIÓN DE AYUDA ▼▼▼ ---
private fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}



// --- COMPONENTES REUTILIZABLES (Section, LocationInputs, PetSelector, SegmentedButtons) ---
// Estos componentes no necesitan cambios y puedes pegarlos aquí desde la respuesta anterior.
// ...
@Composable
fun Section(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge/* style = MaterialTheme.typography.titleMedium,*/
        )
        content()
    }
}

@Composable
fun LocationInputs(
    origin: String,
    destination: String,
    onOriginChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = origin,
            onValueChange = onOriginChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Desde") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Lugar de inicio") },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
        OutlinedTextField(
            value = destination,
            onValueChange = onDestinationChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Hasta") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Lugar de destino", tint = Color.Red) },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetSelector(
    pets: List<Pet>,
    selectedPet: Pet?,
    onPetSelected: (Pet) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPet?.name ?: "Selecciona una mascota",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text("Mascota") },
            leadingIcon = { Icon(Icons.Default.Pets, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            pets.forEach { pet ->
                DropdownMenuItem(
                    text = { Text(pet.name) },
                    onClick = {
                        onPetSelected(pet)
                        expanded = false
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = pet.photoResId),
                            contentDescription = pet.name,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SegmentedButtons(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            OutlinedButton(
                onClick = { onOptionSelected(option) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) GuauYellow.copy(alpha = 0.7f) else Color.Transparent
                )
            ) {
                Text(text = option, textAlign = TextAlign.Center, color = if(isDarkTheme) Color.White else Color.Black, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberSheet(
    sheetState: SheetState,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Ingresa tu precio S/. ", fontSize = 20.sp)

            Spacer(Modifier.height(16.dp))

            TextField(
                value = value,
                onValueChange = { if (it.all(Char::isDigit)) onValueChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done), // 1. Define la acción
                keyboardActions = KeyboardActions(onDone = { onConfirm() }), // 2. Llama a onConfirm cuando se presiona
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar")
            }
        }
    }
}

/*// --- PREVISUALIZACIÓN ---
@Preview(showBackground = true, device = "id:pixel_8_pro")
@Composable
fun RequestWalkScreenPreview() {
    AppGuauGoTheme {
        RequestWalkScreen(
            navController = { NavController },
            onProfileClick = {},
            onMyPetsClick = {},
            onLogoutClick = {},
            onRequestWalkClick = {}
        )

    }
}*/



