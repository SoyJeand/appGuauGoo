package com.example.appguaugo.presentation.home

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appguaugo.R
import com.example.appguaugo.ui.theme.AppGuauGoTheme
import com.example.appguaugo.ui.theme.GuauYellow
import com.example.appguaugo.ui.theme.GuauYellowDark
import com.example.appguaugo.viewmodel.MainViewModel
import kotlinx.coroutines.launch

data class Pet(val id: Int, val name: String, val photoResId: Int)
val samplePets = listOf(
    Pet(1, "Rocky", R.drawable.logo_princ_guaoguao),
    Pet(2, "Luna", R.drawable.logo_princ_guaoguao),
    Pet(3, "Max", R.drawable.logo_princ_guaoguao)
)
val walkTypes = listOf("Corto (30 min)", "Normal (1 h)", "Extendido (2 h)")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestWalkScreen(
    onProfileClick: () -> Unit,
    onMyPetsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRequestWalkClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mainViewModel: MainViewModel = viewModel()
    val nombreCliente by mainViewModel.nombreCliente.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                nombreCliente = nombreCliente ?: "",
                onProfileClick = {
                    onProfileClick()
                    scope.launch { drawerState.close() }
                },
                onMyPetsClick = {
                    onMyPetsClick()
                    scope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    onLogoutClick()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        val sheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
        val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

        var origin by remember { mutableStateOf("") }
        var destination by remember { mutableStateOf("") }
        var selectedPet by remember { mutableStateOf<Pet?>(null) }
        var selectedWalkType by remember { mutableStateOf(walkTypes.first()) }
        var observations by remember { mutableStateOf("") }

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                WalkRequestForm(
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
                    onRequestWalkClick = onRequestWalkClick
                )
            },
            sheetPeekHeight = 200.dp,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetShadowElevation = 8.dp,
            sheetContainerColor = Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MapSimulator()

                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Abrir menú"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
    nombreCliente: String,
    onProfileClick: () -> Unit,
    onMyPetsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GuauYellow.copy(alpha = 0.2f))
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_princ_guaoguao),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, GuauYellow, CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = if (nombreCliente.isNotEmpty()) "Hola, $nombreCliente" else "Hola",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Mi Perfil") },
            selected = false,
            onClick = onProfileClick
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Pets, contentDescription = null) },
            label = { Text("Mis Mascotas") },
            selected = false,
            onClick = onMyPetsClick
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Logout, contentDescription = null) },
            label = { Text("Cerrar Sesión") },
            selected = false,
            onClick = onLogoutClick
        )
    }
}

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
            .heightIn(max = 600.dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Solicitar un Paseo",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Section(title = "Recorrido del paseo") {
            LocationInputs(
                origin = origin,
                destination = destination,
                onOriginChange = onOriginChange,
                onDestinationChange = onDestinationChange
            )
        }

        Section(title = "Selecciona tu mascota") {
            PetSelector(
                pets = samplePets,
                selectedPet = selectedPet,
                onPetSelected = onPetSelected
            )
        }

        Section(title = "Tipo de paseo") {
            SegmentedButtons(
                options = walkTypes,
                selectedOption = selectedWalkType,
                onOptionSelected = onWalkTypeSelected
            )
        }

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
                "Solicitar paseo 🐾",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun MapSimulator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDDF0E8)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Marcador de ubicación",
            modifier = Modifier.size(48.dp),
            tint = Color.Red.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Aquí va el mapa interactivo",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White
        )
    }
}

@Composable
fun Section(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold)
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
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = destination,
            onValueChange = onDestinationChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Hasta") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red) },
            shape = RoundedCornerShape(12.dp)
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

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
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
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
                Text(text = option, textAlign = TextAlign.Center, color = Color.Black, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_8_pro")
@Composable
fun RequestWalkScreenPreview() {
    AppGuauGoTheme {
        RequestWalkScreen(
            onProfileClick = {},
            onMyPetsClick = {},
            onLogoutClick = {},
            onRequestWalkClick = {}
        )
    }
}
