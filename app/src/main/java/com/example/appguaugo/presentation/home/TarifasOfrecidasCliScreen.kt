package com.example.appguaugo.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appguaugo.R
import com.example.appguaugo.viewmodel.OfertaPaseador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarifasOfrecidasCliView(
    onNavigateBack: () -> Unit,
    viewModel: FakeTarifasOfrecidasViewModel = viewModel()
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
            Spacer(Modifier.width(25.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(oferta.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "S/ ${"%.2f".format(oferta.precioOfrecido)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column (
            ) {
                Text("Calificacion:", style = MaterialTheme.typography.titleMedium)
                Row (modifier= Modifier,
                    horizontalArrangement = Arrangement.Center) {
                    Text("4.5", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
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

class FakeTarifasOfrecidasViewModel : ViewModel() { private val _ofertas = MutableStateFlow( listOf( OfertaPaseador( id = 16, nombre = "Carlos Pérez", precioOfrecido = 15.0, fotoResId = R.drawable.paseador_default ), OfertaPaseador( id = 100, nombre = "Ana López", precioOfrecido = 18.5, fotoResId = R.drawable.paseador_default  ) ) )
    val ofertas: StateFlow<List<OfertaPaseador>> = _ofertas }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTarifasOfrecidasCliView() {

    // Simulación de un ViewModelStoreOwner para que el preview funcione
    val owner = object : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore = ViewModelStore()
    }

    CompositionLocalProvider(
        LocalViewModelStoreOwner provides owner
    ) {
        val fakeViewModel = FakeTarifasOfrecidasViewModel()

        MaterialTheme {
            TarifasOfrecidasCliView(
                onNavigateBack = {},
                viewModel = fakeViewModel
            )
        }
    }
}
