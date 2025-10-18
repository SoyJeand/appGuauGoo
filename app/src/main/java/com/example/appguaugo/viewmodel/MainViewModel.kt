package com.example.appguaugo.viewmodel

import android.database.sqlite.SQLiteConstraintException
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

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
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


    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerUiState = _registerUiState.asStateFlow()

    fun insertCliente(cliente: ClienteEntity) {
        // Inicia una coroutina en el scope del ViewModel.
        viewModelScope.launch {
            // 3. Notifica a la UI que estamos empezando a trabajar.
            _registerUiState.value = RegisterUiState.Loading

            try {
                // 4. Ejecuta la operación de base de datos en el hilo de IO (Entrada/Salida).
                val nuevoId = withContext(Dispatchers.IO) {
                    repository.insertCliente(cliente)
                }

                // 5, LA INSERCIÓN FUE EXITOSA.
                // No hubo excepción. Actualiza la UI al estado de Éxito.
                _registerUiState.value = RegisterUiState.Success
                Log.d("REGISTER_VM", "Cliente registrado con éxito. ID: $nuevoId")

            } catch (e: SQLiteConstraintException) {
                // 6. SI HAY UN CONFLICTO (CORREO DUPLICADO), EL CÓDIGO SALTA AQUÍ.
                // Actualiza la UI al estado de Error con un mensaje específico.
                _registerUiState.value = RegisterUiState.Error("Este correo electrónico ya está registrado.")
                Log.w("REGISTER_VM", "Error de constraint al registrar. Probablemente el correo ya existe.", e)

            } catch (e: Exception) {
                // 7. Captura cualquier otro tipo de error inesperado (red, etc.).
                _registerUiState.value = RegisterUiState.Error("Ocurrió un error inesperado.")
                Log.e("REGISTER_VM", "Error desconocido durante el registro.", e)
            }
        }
    }

    fun resetRegisterState() {
        _registerUiState.value = RegisterUiState.Idle
    }


    /*suspend fun insertCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            val filaId = repository.insertCliente(cliente)
            Log.d("ViewModel", "Cliente insertado con ID: $filaId")
//            GuauApp.db.clienteDao().insertCliente(cliente)
        }
    }*/



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


}

