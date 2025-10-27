package com.example.appguaugo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appguaugo.application.GuauApp
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.repository.ClienteRepository
import com.example.appguaugo.presentation.home.HomeScreen
import com.example.appguaugo.presentation.home.RequestWalkScreen
import com.example.appguaugo.presentation.login.LoginScreen
import com.example.appguaugo.presentation.login.OlvidoPasswordScreen
import com.example.appguaugo.presentation.login.RegisterScreen
import com.example.appguaugo.presentation.rating.RatingScreen
import com.example.appguaugo.presentation.search.SearchingScreen
import com.example.appguaugo.presentation.splash.SplashScreen
import com.example.appguaugo.presentation.tracking.TrackingScreen
import com.example.appguaugo.ui.theme.AppGuauGoTheme
import com.example.appguaugo.viewmodel.LoginUiState
import com.example.appguaugo.viewmodel.MainViewModel
import com.example.appguaugo.viewmodel.RegisterUiState

// Asegúrate que el nombre de tu tema sea correcto

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = ClienteRepository(GuauApp.db.clienteDao())
                return MainViewModel(repository) as T
            }
        }
    }

    /*private val viewModel: MainViewModel by viewModels()*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            AppGuauGoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Creamos el controlador de navegación
                    val navController = rememberNavController()

                    // 2. NavHost es el contenedor que cambiará las pantallas
                    NavHost(
                        navController = navController,
                        startDestination = "splash" // La primera pantalla que se mostrará
                    ) {
                        // 3. Definimos cada pantalla (ruta) de nuestra app
                        composable("splash") {
                            SplashScreen(onTimeout = {
                                // Cuando el splash termine, vamos al login
                                navController.navigate("login") {
                                    // Evitamos que el usuario pueda volver al splash con el botón de atrás
                                    popUpTo("splash") { inclusive = true }
                                }
                            })
                        }
                        // En MainActivity.kt, dentro de tu NavHost
                        composable("login") {
                            val scope = rememberCoroutineScope()
                            val context = LocalContext.current
                            val loginState by mainViewModel.loginUiState.collectAsState()

                            LoginScreen(
                                loginState = loginState,
                                onLoginClick = { correo, contrasenha -> /* Tu lógica de ViewModel aquí */
                                    // La UI solo notifica al ViewModel que el botón fue presionado
                                    mainViewModel.validarUsuario(correo, contrasenha)

//                                    /*scope.launch {
//                                        val cliente = mainViewModel.validarUsuario(correo, contrasenha)
//
//                                        // 2. Ahora, toma una decisión basada en el resultado
//                                        if (cliente != null) {
//                                            // ÉXITO: El cliente existe, navega a la pantalla principal
//                                            navController.navigate("home") {
//                                                popUpTo("login") { inclusive = true }
//                                            }
//                                        } else {
//                                            // FRACASO: El cliente es null, muestra un mensaje de error
//                                            // Usamos withContext para asegurarnos de que el Toast se muestre en el hilo principal
//                                            withContext(Dispatchers.Main) {
//                                                Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
//                                            }
//                                        }
//                                    }*/
                                               },

                                onGoogleLoginClick = { /* Lógica de login con Google */ },
                                onRegisterClick = { navController.navigate("register") },
                                onForgotPasswordClick = { navController.navigate("olvido_password") }
                            )

                            // 3. LA NAVEGACIÓN Y LOS TOASTS SE GESTIONAN AQUÍ, REACCIONANDO AL ESTADO
                            // LaunchedEffect se ejecuta cuando 'loginState' cambia.
                            LaunchedEffect(loginState) {
                                when (val state = loginState) { // Usamos 'state' para smart casting
                                    is LoginUiState.Success -> {
                                        // El ViewModel dijo que el login fue exitoso, entonces navegamos
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                        // Opcional: Resetea el estado para no volver a navegar si la pantalla se recompone
                                        mainViewModel.resetLoginState()
                                    }
                                    is LoginUiState.Error -> {
                                        // El ViewModel dijo que hubo un error, entonces mostramos el Toast
                                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                                        mainViewModel.resetLoginState()
                                    }
                                    else -> {
                                        // No hacer nada en los estados Idle o Loading
                                    }
                                }
                            }


                        }

                        composable("register") {
                            val scope = rememberCoroutineScope()
                            val registerState by mainViewModel.registerUiState.collectAsState()
                            val context = LocalContext.current
                            // He renombrado tu pantalla a 'RegisterScreen' para que coincida con el nombre del archivo.
                            // Si la tuya se llama 'RegisterrScreen', déjalo así.
                            RegisterScreen(
                                registerState = registerState,
                                onRegisterClick = { nombres, apellidos, correo, contrasenha, fecNacimientoStr, direccion, telefono ->
                                    // --- VALIDACIÓN DE CAMPOS VACÍOS (Buena práctica) ---
                                    if (nombres.isBlank() || apellidos.isBlank() || correo.isBlank() || contrasenha.isBlank() || fecNacimientoStr.isNullOrBlank()) {
                                        Toast.makeText(context, "Por favor, completa los campos requeridos", Toast.LENGTH_SHORT).show()
                                        return@RegisterScreen // Detiene la ejecución aquí
                                    }



                                    // 3. Llamar a la función correcta en el ViewModel. <-- CAMBIO
                                    // La función que contiene el try-catch y maneja los estados es 'registrarNuevoCliente'.
                                    mainViewModel.insertCliente(
                                        nombres = nombres,
                                        apellidos = apellidos,
                                        correo = correo,
                                        contrasenha = contrasenha, // RECUERDA: Lo ideal es encriptar esto.
                                        fecNacimientoStr = fecNacimientoStr,
                                        direccion = direccion,
                                        telefono = telefono
                                    )
                                },
                                onGoToLoginClick = { navController.navigate("login") }
                            )

                            // El LaunchedEffect que tenías ya es perfecto. No necesita cambios.
                            LaunchedEffect(registerState) {
                                when (val state = registerState) {
                                    is RegisterUiState.Success -> {
                                        Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                        mainViewModel.resetRegisterState()
                                    }
                                    is RegisterUiState.Error -> {
                                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                                        mainViewModel.resetRegisterState()
                                    }
                                    // No es necesario hacer nada en los otros estados aquí.
                                    else -> { /* No-op */ }
                                }
                            }
                        }

                        composable("olvido_password") {
                            val context = LocalContext.current

                            // Aquí es donde llamaremos a nuestra nueva pantalla
                            OlvidoPasswordScreen(
                                onSendLinkClick = { email ->
                                    // --- LÓGICA AL ENVIAR EL ENLACE ---
                                    // Aquí iría la llamada a tu ViewModel para iniciar el proceso de recuperación.
                                    // Por ejemplo: mainViewModel.sendPasswordResetEmail(email)

                                    // Por ahora, podemos mostrar un mensaje temporal.
                                    Log.d("ForgotPassword", "Solicitud de recuperación para: $email")
                                    Toast.makeText(
                                        context, // Asegúrate de tener el 'context' disponible
                                        "Enlace de recuperación enviado a $email",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Opcional: Navegar de vuelta al login después de enviar el enlace.
                                    navController.popBackStack()
                                },
                                onBackToLoginClick = {
                                    // --- LÓGICA AL VOLVER AL LOGIN ---
                                    // Simplemente navega hacia atrás en la pila de navegación.
                                    navController.popBackStack()
                                }
                            )
                        }

                        // En tu MainActivity.kt, dentro del NavHost
                        // ... (tus otras rutas como "splash", "login", "register", "forgot_password")
                        composable("home") {
                            RequestWalkScreen(
                                navController = navController,
                                // 1. Acción para el menú: Ir a Perfil
                                onProfileClick = {
                                    navController.navigate("profile")
                                    Log.d("MainActivity", "Navegando a la pantalla de Perfil")
                                },
                                // 2. Acción para el menú: Ir a Mis Mascotas
                                onMyPetsClick = {
                                    navController.navigate("my_pets")
                                    Log.d("MainActivity", "Navegando a la pantalla de Mis Mascotas")
                                },
                                // DE MOMENTO NO SE USARA ESTA LOGICA!!!!
                                /*// 3. Acción para el menú: Cerrar Sesión
                                onLogoutClick = {
                                    // Aquí ejecutas la lógica de cierre de sesión
                                    //mainViewModel.logout()
                                    //Log.d("MainActivity", "Cerrando sesión del usuario.")

                                    // Navegas de vuelta al login, limpiando toda la pila de navegación
                                    *//*navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()*//*
                                },*/
                                // 4. Acción del botón principal del formulario
                                onRequestWalkClick = {
                                    //.d("MainActivity", "Solicitando un nuevo paseo...")
                                    //Toast.makeText(context, "Buscando un paseador...", Toast.LENGTH_SHORT).show()
                                    // Aquí llamarías a tu ViewModel para procesar la solicitud
                                    // mainViewModel.requestWalk(origen, destino, mascota, etc.)
                                }
                            )
                        }

                        // --- ▼▼▼ DESTINOS PARA LAS OPCIONES DEL MENÚ ▼▼▼ ---

                        // Pantalla de Perfil (Composable temporal como placeholder)
                        composable("profile") {
                            // Aquí deberías poner tu Composable `ProfileScreen()` cuando lo crees
                            ScaffoldWithBackButton(navController = navController, title = "Mi Perfil") {
                                Text("Contenido de la Pantalla de Perfil")
                            }
                        }

                        // Pantalla de Mascotas (Composable temporal como placeholder)
                        composable("my_pets") {
                            // Aquí deberías poner tu Composable `MyPetsScreen()` cuando lo crees
                            ScaffoldWithBackButton(navController = navController, title = "Mis Mascotas") {
                                Text("Contenido de la Pantalla de Mis Mascotas")
                            }
                        }


                        composable("searching") {
                            // En un caso real, aquí tendrías lógica para saber si se encontró un paseador
                            // Para este ejemplo, vamos a simular que se encontró y pasamos a la pantalla de tracking.
                            SearchingScreen()
                            // Aquí podrías añadir un LaunchedEffect para navegar después de unos segundos
                            // navController.navigate("tracking")
                        }
                        composable("tracking") {
                            TrackingScreen(onWalkFinished = {
                                navController.navigate("rating")
                            })
                        }
                        composable("rating") {
                            RatingScreen(onRatingSubmitted = {
                                // Después de calificar, volvemos al home
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            })
                        }
                        // Aquí añadirías las rutas para profile, history, settings, etc.
                    }
                }
            }
        }
    }
}
// ... (Aquí termina el código de tu MainActivity)


// COMPONENTE DE AYUDA PARA PANTALLAS SECUNDARIAS
// Crea un Scaffold con una barra superior que incluye un título y un botón de "atrás".
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithBackButton(
    navController: androidx.navigation.NavController,
    title: String,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    // Este es el botón de la flecha para volver
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Contenedor para el contenido de la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
