package com.example.appguaugo.data.repository

import com.example.appguaugo.data.dao.ClienteDao
import com.example.appguaugo.data.dao.MascotaDao
import com.example.appguaugo.data.dao.SolicitudPaseoDao
import com.example.appguaugo.data.entity.ClienteEntity
import com.example.appguaugo.data.entity.MascotaEntity
import com.example.appguaugo.data.entity.SolicitudPaseoEntity
import kotlinx.coroutines.flow.Flow

class ClienteRepository(
    private val clienteDao: ClienteDao,
    private val mascotaDao: MascotaDao,
    private val solicitudPaseoDao: SolicitudPaseoDao
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

    // FUNCIONES DE LA SOLICITUD DE PASEO CLI
    suspend fun insertSolicitudPaseo(solicitud: SolicitudPaseoEntity) {
        solicitudPaseoDao.insertSolicitud(solicitud)
    }

//    /*// Llama a la función correspondiente en el DAO para obtener todos los clientes.
//    suspend fun getAllClientes(): List<ClienteEntity> {
//        return clienteDao.getAll()
//    }
//
//    // Llama a la función correspondiente en el DAO para obtener clientes por sus IDs.
//    suspend fun getAllClientesById(clienteIds: IntArray): List<ClienteEntity> {
//        return clienteDao.getAllById(clienteIds)
//    }*/
}