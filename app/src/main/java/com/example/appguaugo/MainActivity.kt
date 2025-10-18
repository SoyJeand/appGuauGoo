package com.example.appguaugo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appguaugo.application.GuauApp
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.repository.ClienteRepository
import com.example.appguaugo.presentation.home.HomeScreen
import com.example.appguaugo.presentation.login.LoginScreen
import com.example.appguaugo.presentation.login.LoginnScreen
import com.example.appguaugo.presentation.login.RegisterScreen
import com.example.appguaugo.presentation.rating.RatingScreen
import com.example.appguaugo.presentation.search.SearchingScreen
import com.example.appguaugo.presentation.splash.SplashScreen
import com.example.appguaugo.presentation.tracking.TrackingScreen
import com.example.appguaugo.ui.theme.AppGuauGoTheme
import com.example.appguaugo.viewmodel.LoginUiState
import com.example.appguaugo.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

                            LoginnScreen(
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
                                onForgotPasswordClick = { /* Navegar a la pantalla de olvidar contraseña */ }
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


                        /*composable("login") {
                            val scope = rememberCoroutineScope()
                            val context = LocalContext.current
                            LoginScreen(
                                onLoginSuccess = { correo, contrasenha ->

                                    scope.launch {
                                        val cliente = mainViewModel.validarUsuario(correo, contrasenha)

                                        // 2. Ahora, toma una decisión basada en el resultado
                                        if (cliente != null) {
                                            // ÉXITO: El cliente existe, navega a la pantalla principal
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            // FRACASO: El cliente es null, muestra un mensaje de error
                                            // Usamos withContext para asegurarnos de que el Toast se muestre en el hilo principal
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }

                                    *//*scope.launch {
                                        GuauApp.db.clienteDao().validarCliente(correo, contrasenha)
                                    }*//*

                                    // Cuando el login sea exitoso, vamos al home
                                    *//*navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }*//*
                            }, // Aquí le pasas la "instrucción" de lo que debe hacer
                                onNavigateToRegister = {
                                    navController.navigate("register") // <-- ¡LA MAGIA OCURRE AQUÍ!
                                }
                            )
                        }*/
                        // Dentro de tu NavHost en MainActivity.kt

                        composable("register") {
                            val scope = rememberCoroutineScope()
                            RegisterScreen(
                                onRegisterClick = { nombres, apellidos, correo, contrasenha, nTelefono ->
                                    // 1. Crea la instancia de ClienteEntity
                                    val nuevoCliente = ClienteEntity(
                                        nombres = nombres,
                                        apellidos = apellidos,
                                        correo = correo,
                                        contrasenha = contrasenha, // Recuerda encriptar la contraseña en una app real
                                        nTelefono = nTelefono
                                    )

                                    // --- LÓGICA DE ROOM ---
                                    scope.launch {
                                        val cliente = mainViewModel.insertCliente(nuevoCliente)
                                    }

                                    // 3. Navega a la pantalla de login
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true } // Para que el usuario no vuelva atrás
                                    }
                                },
                                onGoToLoginClick = {
                                    navController.navigate("login")
                                }
                            )
                        }

                        composable("home") {
                            HomeScreen(onSearchClick = {
                                navController.navigate("searching")
                            })
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