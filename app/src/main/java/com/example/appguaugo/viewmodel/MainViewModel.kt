package com.example.appguaugo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.application.GuauApp
import com.example.appguaugo.data.entity.ClienteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//  import androidx.lifecycle.ViewModelProvider
import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

sealed class LoginUiState {
    object Idle : LoginUiState() // Estado inicial, no haciendo nada
    object Loading : LoginUiState() // Estado de carga, para mostrar un spinner
    object Success : LoginUiState() // Estado de éxito en el login
    data class Error(val message: String) : LoginUiState() // Estado de error con un mensaje
}
class MainViewModel(private val repository: ClienteRepository): ViewModel() {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow() // La UI observará esta variable

    fun validarUsuario(correo: String, contrasenha: String) {
        // Lanza la coroutina DENTRO del ViewModel
        viewModelScope.launch {
            // Cambia el estado a "Cargando"
            _loginUiState.value = LoginUiState.Loading

            // Llama a tu función suspend
            val cliente = withContext(Dispatchers.IO) {
                repository.validarCliente(correo, contrasenha)
            }

            // 4. EL VIEWMODEL TOMA LA DECISIÓN
            if (cliente != null) {
                // Éxito: Actualiza el estado a Success
                _loginUiState.value = LoginUiState.Success
            } else {
                // Fracaso: Actualiza el estado a Error
                _loginUiState.value = LoginUiState.Error("Correo o contraseña incorrectos")
            }
        }
    }

    fun resetLoginState() {
        _loginUiState.value = LoginUiState.Idle
    }

    suspend fun insertCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            val filaId = repository.insertCliente(cliente)
            Log.d("ViewModel", "Cliente insertado con ID: $filaId")
//            GuauApp.db.clienteDao().insertCliente(cliente)
        }
    }

   /*suspend fun validarUsuario(correo: String, contrasenha: String): ClienteEntity? {

       return withContext(Dispatchers.IO) {
            repository.validarCliente(correo, contrasenha)  // Ahora llamamos al repositorio
        }
    }*/



    /*fun insertCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            val idNuevaFila = clienteDao.insertCliente(cliente)
            if (idNuevaFila > 0) {
                // La inserción fue exitosa
                println("Cliente registrado con ID: $idNuevaFila")
            } else {
                // Ocurrió un error en la inserción
                println("Error al registrar el cliente.")
            }
        }
    }*/



    /*fun insertoCliente() {
        val clienteEntity = ClienteEntity(
            nombres = "Fulano",
            apellidos = "Perez",
            correo = "jeango@gmail.com",
            contrasenha = "123456",
            nTelefono = "987654321"
        )

        viewModelScope.launch(Dispatchers.IO) {
            GuauApp.db.clienteDao().insertCliente(clienteEntity)
        }
    }*/




    fun insertClienteList() {
        val clientes = listOf(
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perez", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perezz", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321"),
            ClienteEntity(nombres = "Fulano", apellidos = "Perezzz", correo = "jean@gmail.com", contrasenha = "123456", nTelefono = "987654321")

        )

        viewModelScope.launch(Dispatchers.IO) {
            GuauApp.db.clienteDao().insertClienteList(clientes)
        }
    }

    fun updateCliente() {
        val clienteEntity = ClienteEntity(idcli = 10, nombres = "Fulano", apellidos = "Perez", correo = "jeandelacruz.214@gmail.com", contrasenha = "ianir.666", nTelefono = "987654321")

        viewModelScope.launch(Dispatchers.IO) {
            GuauApp.db.clienteDao().updateCliente(clienteEntity)
        }
    }
    fun deleteCliente() {
        val clienteEntity = ClienteEntity(idcli = 5, "", "", "","","")

        viewModelScope.launch(Dispatchers.IO) {
            GuauApp.db.clienteDao().deleteCliente(clienteEntity)
        }
    }

    fun getAllCliente() {

        viewModelScope.launch(Dispatchers.IO) {
            val clienteList = GuauApp.db.clienteDao().getAll()
            clienteList.forEach{
                Log.d("user", "user = $it")
            }
        }
    }

    fun getAllByIdCliente() {

        viewModelScope.launch(Dispatchers.IO) {
            GuauApp.db.clienteDao().getAllById(intArrayOf(5))
        }
    }

}

