package com.example.appguaugo.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appguaugo.R // Asegúrate de tener una imagen de avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Perfil") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navegar a pantalla de edición */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.avatar), // Cambia por tu imagen
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ana García", // Esto vendría de los datos del usuario
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "ana.garcia@email.com",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            Divider()
            // Aquí puedes añadir más información como "Mis Mascotas", etc.
        }
    }
}
