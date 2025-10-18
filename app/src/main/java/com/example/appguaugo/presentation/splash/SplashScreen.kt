package com.example.appguaugo.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.appguaugo.R // Asegúrate de tener tu logo en res/drawable

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Este efecto se lanza una sola vez cuando el composable aparece en pantalla
    LaunchedEffect(Unit) {
        delay(3000) // Espera 3 segundos
        onTimeout() // Llama a la función para navegar a la siguiente pantalla
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_guaugo), // Cambia por tu logo
            contentDescription = "Logo de la App",
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = "PaseaPerros",
            fontSize = 32.sp
        )
    }
}