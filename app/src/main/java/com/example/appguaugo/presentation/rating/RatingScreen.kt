package com.example.appguaugo.presentation.rating

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingScreen(onRatingSubmitted: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Califica el paseo!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de estrellas
        Row {
            (1..5).forEach { index ->
                IconButton(onClick = { rating = index }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrella $index",
                        tint = if (index <= rating) Color.Yellow else Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Deja un comentario (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRatingSubmitted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Calificación")
        }
    }
}