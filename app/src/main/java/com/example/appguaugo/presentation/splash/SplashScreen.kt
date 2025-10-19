package com.example.appguaugo.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.R // Asegúrate de que esta sea la ruta correcta a tu archivo R
import com.example.appguaugo.ui.theme.AppGuauGoTheme // O el nombre de tu tema
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // Este LaunchedEffect ejecuta un bloque de código una sola vez cuando el Composable aparece.
    // Es perfecto para la lógica de una pantalla de bienvenida.
    LaunchedEffect(Unit) {
        delay(3000) // Espera 3 segundos (3000 milisegundos)
        onTimeout() // Llama a la función para navegar a la siguiente pantalla
    }

    // Usamos un Box para centrar todo fácilmente
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Fondo blanco
        contentAlignment = Alignment.Center // Centra todo el contenido
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. EL LOGO
            // Aquí es donde pondrás tu imagen.
            // Asegúrate de tener un 'logo_princ_guaugo.png' (o similar) en tu carpeta 'res/drawable'.
            Image(
                painter = painterResource(id = R.drawable.logo_princ_guaoguao),
                contentDescription = "Logo del Proyecto GuaoGuao",
                modifier = Modifier.size(150.dp) // Ajusta el tamaño según necesites
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. EL TÍTULO DEL PROYECTO
            Text(
                text = "Proyecto GuaoGuao",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107) // Un color amarillo similar al de la imagen
            )

            // Usamos un Spacer para empujar el texto inferior hacia abajo
            Spacer(modifier = Modifier.height(150.dp))
        }

        // 3. EL ESLOGAN EN LA PARTE INFERIOR
        // Lo colocamos en otro Box alineado en la parte inferior para posicionarlo mejor.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp), // Espacio desde el borde inferior
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "¡Tu mascota en las mejores manos!",
                fontSize = 16.sp,
                color = Color.Gray // Un color gris para el texto secundario
            )
        }
    }
}


// --- PREVISUALIZACIÓN ---
// Te permite ver cómo se ve la pantalla sin tener que ejecutar la app.
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    AppGuauGoTheme {
        SplashScreen(onTimeout = {}) // Para la preview, no necesita hacer nada al terminar
    }
}
