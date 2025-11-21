package com.example.appguaugo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


// Set of Material typography styles to start with

// Definimos una familia de fuentes que será la por defecto.
// FontFamily.Default es una buena opción que usa la fuente del sistema (como Roboto).
val AppFontFamily = FontFamily.Default
val Typography = Typography(
    // Título Grande (Ej: para encabezados de pantalla)
    displayLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold, // Especificamos que este estilo usa la fuente Bold
        fontSize = 30.sp
    ),
    // Título para secciones
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.ExtraBold, // También Bold
        fontSize = 22.sp
    ),
    // Texto principal del cuerpo
    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal, // Este es el peso normal
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Texto para botones
    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold, // Botones en negrita
        fontSize = 16.sp
    ),

    bodySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.ExtraBold, // Este es el peso normal
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Puedes añadir más estilos como titleMedium, bodyMedium, etc. */
)