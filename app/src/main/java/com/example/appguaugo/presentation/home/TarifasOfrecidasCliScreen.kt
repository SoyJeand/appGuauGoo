package com.example.appguaugo.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appguaugo.R
import com.example.appguaugo.viewmodel.OfertaPaseador
import com.example.appguaugo.viewmodel.TarifasOfrecidasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarifasOfrecidasCliView(
    onNavigateBack: () -> Unit,
    viewModel: TarifasOfrecidasViewModel = viewModel()
) {
    val ofertas by viewModel.ofertas.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ofertas de Paseadores") })
        },
        bottomBar = {
            Button(
                onClick = {
                    Toast.makeText(context, "Solicitud Cancelada", Toast.LENGTH_SHORT).show()
                    onNavigateBack() // Vuelve a la pantalla anterior
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("CANCELAR SOLICITUD")
            }
        }
    ) { paddingValues ->
        if (ofertas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
                Text("Buscando ofertas...", modifier = Modifier.padding(top = 60.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(ofertas) { oferta ->
                    OfertaPaseadorItem(oferta = oferta)
                }
            }
        }
    }
}

@Composable
fun OfertaPaseadorItem(oferta: OfertaPaseador) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = oferta.fotoResId),
                contentDescription = oferta.nombre,
                modifier = Modifier.size(60.dp).clip(CircleShape).border(1.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(oferta.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "S/ ${"%.2f".format(oferta.precioOfrecido)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                Button(
                    onClick = { Toast.makeText(context, "Aceptaste a ${oferta.nombre}", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.padding(end = 8.dp)
                ) { Text("Aceptar") }

                OutlinedButton(
                    onClick = { Toast.makeText(context, "Rechazaste a ${oferta.nombre}", Toast.LENGTH_SHORT).show() }
                ) { Text("Rechazar") }
            }
        }
    }
}
