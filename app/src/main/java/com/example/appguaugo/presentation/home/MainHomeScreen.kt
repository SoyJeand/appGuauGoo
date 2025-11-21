package com.example.appguaugo.presentation.home
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appguaugo.application.GuauApp
import com.example.appguaugo.application.UserRole
import com.example.appguaugo.application.UserSessionManager
import com.example.appguaugo.data.repository.ClienteRepository
import com.example.appguaugo.presentation.walker_home.WalkerHomeScreen
import com.example.appguaugo.viewmodel.ProfileUiState
import com.example.appguaugo.viewmodel.ProfileViewModel
import com.example.appguaugo.viewmodel.ProfileViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    navController: NavController
    // Ya no necesitamos pasar todos los eventos, esta pantalla los gestionará
) {
    // --- 1. Observar el rol actual del usuario ---
    val context = LocalContext.current // Lo necesitaremos para el logout
    val prefs = remember { context.getSharedPreferences("mi_app_prefs", Context.MODE_PRIVATE) }
    val loggedInUserId = remember { prefs.getInt("logged_in_user_id", -1) }

    val repository = remember {
        // Asumiendo que GuauApp.db está disponible globalmente como lo hemos manejado antes
        ClienteRepository(
            GuauApp.db.clienteDao(),
            GuauApp.db.mascotaDao(),
            GuauApp.db.solicitudPaseoDao()
        )
    }

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(repository, loggedInUserId)
    )

    // --- ▼▼▼ 3. OBSERVA EL ESTADO DEL VIEWMODEL PARA OBTENER EL NOMBRE ▼▼▼ ---
    val profileState by profileViewModel.uiState.collectAsState()
    val userName = when (val state = profileState) {
        is ProfileUiState.Success -> {
            // Si los datos se cargaron con éxito, usamos el nombre real del usuario
            state.user.nombres
        }
        is ProfileUiState.Loading -> {
            // Mientras los datos cargan, mostramos un texto temporal
            "Cargando..."
        }
        is ProfileUiState.Error -> {
            // Si hay un error, lo indicamos
            "Error"
        }
    }

    // --- Observar el rol actual (sin cambios) ---
    val currentRole by UserSessionManager.currentRole.collectAsState()

    // --- Estado para el Drawer (sin cambios) ---
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // --- 3. Definir los eventos de navegación ---
    val onProfileClick: () -> Unit = {
        if (loggedInUserId != -1) {
            // Construimos la ruta dinámica como la espera el NavHost: "profile/{userId}"
            navController.navigate("profile/$loggedInUserId")
        } else {
            // Manejo de error: si no hay ID, no navegamos y quizás mostramos un mensaje.
            // Esto no debería pasar si el usuario está logueado.
            Toast.makeText(context, "Error: No se pudo encontrar el ID de usuario.", Toast.LENGTH_SHORT).show()
        }
        scope.launch { drawerState.close() }
    }
    val onMyPetsClick: () -> Unit = {
        navController.navigate("my_pets")
        scope.launch { drawerState.close() }
    }
    val onLogoutClick: () -> Unit = {
        // Lógica completa de logout
        prefs.edit().clear().apply()
        navController.navigate("login") {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                userName =  userName, // TODO: Obtener de un ViewModel
                // --- Pasando los eventos recién definidos ---
                onProfileClick = onProfileClick,
                onMyPetsClick = onMyPetsClick,
                onLogoutClick = onLogoutClick,
                // --- El resto no cambia ---
                currentRole = currentRole,
                onSwitchRole = { UserSessionManager.switchRole() }
            )
        }
    ) {
        // --- 5. Lógica condicional para mostrar la pantalla correcta ---
        when (currentRole) {
            UserRole.CLIENTE -> {
                RequestWalkScreen(
                    // Pasamos solo los eventos necesarios para esta pantalla
                    navController = navController,
                    openDrawer = { scope.launch { drawerState.open() } }
                    // Adapta RequestWalkScreen para que no maneje el drawer completo, solo lo abra
                )
            }
            UserRole.PASEADOR -> {
                WalkerHomeScreen(
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}
