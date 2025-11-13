package com.example.appguaugo.data.repository

import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.dao.MascotaDao
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

class ClienteRepository(
    private val clienteDao: ClienteDao,
    private val mascotaDao: MascotaDao
) {

    // Llama a la función correspondiente en el DAO para insertar un cliente.
    suspend fun insertCliente(cliente: ClienteEntity): Long {
        return clienteDao.insertCliente(cliente)
    }

    // Llama a la función correspondiente en el DAO para validar un cliente.
    suspend fun validarCliente(correo: String, contrasenha: String): ClienteEntity? {
        return clienteDao.validarCliente(correo, contrasenha)
    }

    fun getClienteById(userId: Int): Flow<ClienteEntity?> {
        return clienteDao.getClienteById(userId)
    }

    // FUNCIONES DE LAS MASCOTAS
    fun getMascotasByDuenoId(duenoId: Int): Flow<List<MascotaEntity>> {
        return mascotaDao.getMascotasByDuenoId(duenoId)
    }

    suspend fun insertMascota(mascota: MascotaEntity) {
        mascotaDao.insertMascota(mascota)
    }

    suspend fun updateCliente(cliente: ClienteEntity): Int {
        return clienteDao.updateCliente(cliente)
    }

    // --- SCRUM 13: AÑADIR ESTAS FUNCIONES ---
    suspend fun checkEmailExists(email: String): Boolean {
        return clienteDao.checkEmailExists(email)
    }

    suspend fun getClienteByEmail(email: String): ClienteEntity? {
        return clienteDao.getClienteByEmail(email)
    }
}