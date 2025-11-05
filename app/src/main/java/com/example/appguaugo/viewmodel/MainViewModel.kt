package com.example.appguaugo.viewmodel

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appguaugo.data.entity.ClienteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//  import androidx.lifecycle.ViewModelProvider
import com.example.appguaugo.data.repository.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


sealed class LoginUiState {
    object Idle : LoginUiState() // Estado inicial, no haciendo nada
    object Loading : LoginUiState() // Estado de carga, para mostrar un spinner
    //object Success : LoginUiState() // Estado de éxito en el login
    data class Success(val user: ClienteEntity) : LoginUiState()
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
        viewModelScope.launch {
        _loginUiState.value = LoginUiState.Loading

        try {
            // La validación se hace en el hilo de IO.
            val cliente = withContext(Dispatchers.IO) {
                repository.validarCliente(correo, contrasenha)
            }

            // La decisión se toma basada en el resultado.
            if (cliente != null) {
                // ÉXITO: El estado ahora transporta el objeto ClienteEntity.
                _loginUiState.value = LoginUiState.Success(cliente)
                Log.d("LOGIN_VM", "Login exitoso para el usuario ID: ${cliente.id}")
            } else {
                // FRACASO: El usuario no fue encontrado o la contraseña es incorrecta.
                _loginUiState.value = LoginUiState.Error("Correo o contraseña incorrectos")
            }
        } catch (e: Exception) {
            // Manejo de cualquier otro error inesperado durante la validación.
            _loginUiState.value = LoginUiState.Error("Ocurrió un error inesperado durante el login.")
            Log.e("LOGIN_VM", "Error desconocido durante la validación.", e)
        }
        // Lanza la coroutina DENTRO del ViewModel
        /*viewModelScope.launch {
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
            }*/
        }
    }

    fun resetLoginState() {
        _loginUiState.value = LoginUiState.Idle
    }


    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerUiState = _registerUiState.asStateFlow()

    fun insertCliente(
        nombres: String,
        apellidos: String,
        correo: String,
        contrasenha: String,
        fecNacimientoStr: String?,
        direccion: String?,
        telefono: String?
    ) {
        viewModelScope.launch {
            // 3. Notifica a la UI que estamos empezando a trabajar.
            _registerUiState.value = RegisterUiState.Loading

            var fechaNacimientoDate: Date?
            try {
                if (!fecNacimientoStr.isNullOrBlank()) {

                    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
                    fechaNacimientoDate = formato.parse(fecNacimientoStr)
                }else {
                    // Si el texto está vacío, la fecha es null.
                    fechaNacimientoDate = null
                }

                // 3. (Opcional pero recomendado) Verificación de nulos post-parseo.
                // A veces, incluso si no hay excepción, el parseo puede resultar en null.
                if (!fecNacimientoStr.isNullOrBlank() && fechaNacimientoDate == null) {
                    // Esto cubre un caso raro donde el parseo falla silenciosamente.
                    _registerUiState.value = RegisterUiState.Error("Formato de fecha inválido.")
                    return@launch
                }

                // --- 4. NUEVO: Creación de la Entidad DENTRO del ViewModel ---
                val nuevoCliente = ClienteEntity(
                    nombres = nombres,
                    apellidos = apellidos,
                    correo = correo,
                    contrasenha = contrasenha,
                    fecNacimiento = fechaNacimientoDate, // Usamos el objeto Date? convertido
                    direccion = direccion,
                    telefono = telefono
                )

                // 5. Ejecuta la inserción en la base de datos (igual que antes, pero con el nuevo objeto)
                val nuevoId = withContext(Dispatchers.IO) {
                    repository.insertCliente(nuevoCliente)
                }

                // 6. Notifica a la UI del éxito (igual que antes)
                _registerUiState.value = RegisterUiState.Success
                Log.d("REGISTER_VM", "Cliente registrado con éxito. ID: $nuevoId")

            } catch (e: SQLiteConstraintException) {
                // 7. Manejo del error de correo duplicado (igual que antes)
                _registerUiState.value = RegisterUiState.Error("Este correo electrónico ya está registrado.")
                Log.w("REGISTER_VM", "Error de constraint al registrar. Probablemente el correo ya existe.", e)

            } catch (e: Exception) {
                // 8. Manejo de cualquier otro error inesperado (igual que antes)
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

